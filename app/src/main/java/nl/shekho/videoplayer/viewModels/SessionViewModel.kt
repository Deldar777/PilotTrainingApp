package nl.shekho.videoplayer.viewModels

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.MimeTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import nl.shekho.videoplayer.api.*
import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.models.*
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime


@ExperimentalTime
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker,
    private val apiService: ApiService,
    private val sessionMapper: SessionMapper,
    private val userMapper: UserMapper,
    private val sessionPropertiesMapper: SessionPropertiesMapper,
    private val logBookMapper: LogBookMapper,
    private val apiMediaService: ApiMediaService,
    val player: Player,
) : ViewModel() {

    init {
        addPlayerListeners()
    }

    //Live events states
    var liveStreamingLoading: Boolean by mutableStateOf(false)
    var liveStreamingPlaying: Boolean by mutableStateOf(false)
    var liveStreamingSettingUp: Boolean by mutableStateOf(false)
    var HLS: MutableState<String> =
        mutableStateOf("https://vrefsolutionsdownload.blob.core.windows.net/trainevids/OVERVIEW.mp4")

    // Modify and read feedback and rating
    var selectedParticipantTabIndex: MutableState<Int> = mutableStateOf(1)
    var currentRating: MutableState<Int> = mutableStateOf(0)
    var currentFeedback: MutableState<String> = mutableStateOf("")
    var saveChangesAsked: Boolean by mutableStateOf(false)
    var saveChangesSucceeded: Boolean by mutableStateOf(false)
    var savingChanges: Boolean by mutableStateOf(false)

    //Alert dialog
    var openDialog = mutableStateOf(false)
    var savingSession: Boolean by mutableStateOf(false)
    var saveSessionAsked: Boolean by mutableStateOf(false)
    var savingSessionSucceeded: Boolean by mutableStateOf(false)

    //Events automation
    var secondsPassed: Int by mutableStateOf(0)
    var maxNumberOfEvents = 10
    var cycleTimeInSeconds = 10
    var generatedEvents = 0

    //Review window
    private val mutableUsers = MutableStateFlow<Result<List<User>>?>(null)
    val users: StateFlow<Result<List<User>>?> = mutableUsers

    //Session feedback variables
    var loading = mutableStateOf(false)
    var succeeded: Boolean by mutableStateOf(false)
    var failed: String by mutableStateOf("")
    var createSessionAsked: Boolean by mutableStateOf(false)

    //Add note button states
    var addNoteButtonEnabled: MutableState<Boolean> = mutableStateOf(false)

    //New session and review windows
    var showNewSessionWindow: MutableState<Boolean> = mutableStateOf(false)
    var showReviewWindow: MutableState<Boolean> = mutableStateOf(false)
    var showEmptyReview: MutableState<Boolean> = mutableStateOf(true)

    //Sessions
    private var mutableSessions = MutableStateFlow<Result<List<Session>>?>(null)
    var sessions: StateFlow<Result<List<Session>>?> = mutableSessions
    var selectedSession =
        mutableStateOf(Session("", LocalDateTime.now().toString(), null, null, null))
    var runningSession: Session? by mutableStateOf(null)
    var selectedSessionIndex = mutableStateOf(100)
    var token = mutableStateOf("")

    //Events
    var sessionProperties: SessionProperties? by mutableStateOf(null)
    private var mutableLogBook = MutableStateFlow<Result<LogBook>?>(null)
    var logBook: StateFlow<Result<LogBook>?> = mutableLogBook
    var selectedEvent = mutableStateOf(
        Event(
            "MARKED_EVENT",
            "LOG_BOOK",
            0,
            EventType.MARKED_EVENT.name,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    )
    var selectedItemIndex = mutableStateOf(100)
    var altitude: Int by mutableStateOf(0)

    fun resetViewWindowsValues() {
        showNewSessionWindow.value = false
        showReviewWindow.value = false
        showEmptyReview.value = true
        runningSession = null
    }

    //Exo player functions
    fun startLiveStreaming(mediaUrl: String) {
        //Creating a media item of HLS Type
        val mediaItem = MediaItem.Builder().setUri(mediaUrl)
            .setMimeType(MimeTypes.APPLICATION_M3U8) //m3u8 is the extension used with HLS sources
            .build()

        player.setMediaItem(mediaItem)

        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ONE //repeating the video from start after it's over
        player.playWhenReady = true
    }

    fun fetchVideoFromUrl(videoUrl: String) {
        val videoURI: Uri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(videoURI)
        player.addMediaItem(MediaItem.fromUri(videoURI))
        player.setMediaItem(mediaItem)
        player.repeatMode = Player.REPEAT_MODE_ONE //repeating the video from start after it's over
        player.prepare()
        player.playWhenReady = true
    }

    //Add event listener to the player to update loading status
    private fun addPlayerListeners() {
        player.addListener(object : Player.Listener {
            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                liveStreamingLoading = isLoading
            }
        })

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                liveStreamingPlaying = isPlaying
            }
        })
    }


    fun endSession(sessionId: String, token: String, userId: String) {
        viewModelScope.launch {
            savingSession = true
            try {
                val response = apiService.updateSessionStatusById(
                    sessionId = sessionId,
                    token = token,
                )

                if (response.isSuccessful) {

                    //Only out commented because we are working with mp4 for the assessment
                    //If the session status stopped successfully, then stop the live streaming
                    stopLiveEvent(token = token)

                    savingSessionSucceeded = true
                    fetchSessionsByUserId(
                        userId = userId, token = token
                    )
                }

            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }
            delay(4000)
            savingSession = false
        }
    }

    fun createSession(newSessionEntity: NewSessionEntity, token: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = apiService.createSession(
                    body = newSessionEntity,
                    token = token,
                )

                val session = response.body()
                if (response.isSuccessful && session != null) {
                    succeeded = true
                    val sessionMapped = sessionMapper.mapEntityToModel(session)
                    runningSession = sessionMapped

                    postVideo(
                        videoRequestEntity = VideoRequestEntity(
                            sessionId = session.id
                        ), token = token
                    )
                } else {
                    failed = response.message()
                }
            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }
            loading.value = false
        }
    }

    fun postVideo(videoRequestEntity: VideoRequestEntity, token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.postVideo(
                    body = videoRequestEntity,
                    token = token,
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    val sessionPropertiesMapped = sessionPropertiesMapper.mapEntityToModel(body)
                    sessionProperties = sessionPropertiesMapped

                    //Start the live event
                    startLiveStreamingProcess(
                        token = token,
                        videoId = body.id
                    )

                    //Pass mp4 video to the player instead of livestreaming
//                    fetchVideoFromUrl("https://vrefsolutionsdownload.blob.core.windows.net/trainevids/OVERVIEW.mp4")

                } else {
                    failed = response.message()
                }

            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }
            loading.value = false
        }
    }

    fun getVideo(sessionId: String, token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getVideoBySessionId(
                    sessionId = sessionId,
                    token = token,
                )
                val body = response.body()

                if (response.isSuccessful && body != null) {
                    val sessionPropertiesMapped = sessionPropertiesMapper.mapEntityToModel(body[0])
                    sessionProperties = sessionPropertiesMapped

                    //Pass the fetched url to the player
                    val fetchedUrl = body[0].videoURL
                    if (fetchedUrl == "https://vrefsolutionsdownload.blob.core.windows.net/trainevids/OVERVIEW.mp4") {
                        fetchVideoFromUrl(fetchedUrl)
                    } else {
                        startLiveStreaming(fetchedUrl)
                    }

                    //If the video entity fetched successfully then get the logbook with events
                    getLogBookById(
                        logBookId = body[0].logbookId, token = token
                    )

                } else {
                    failed = response.message()
                }

            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }
        }
    }

    private fun createEvent(eventRequestEntity: EventRequestEntity, token: String) {
        viewModelScope.launch {
            saveChangesSucceeded = try {
                val response = apiService.createEvent(
                    body = eventRequestEntity,
                    token = token,
                )
                response.isSuccessful && response.body() != null
            } catch (e: java.lang.Exception) {
                false
            }
        }
    }

    private fun createRecord(recordEntity: RecordEntity, token: String) {
        viewModelScope.launch {
            try {
                apiService.createRecord(
                    body = recordEntity,
                    token = token,
                )
            } catch (e: java.lang.Exception) {
                println("Record not created because of this error: $e")
            }
        }
    }

    private fun updateEvent(
        eventRequestEntity: EventRequestEntity, token: String, eventId: String
    ) {
        viewModelScope.launch {
            saveChangesSucceeded = try {
                val response = apiService.updateEvent(
                    eventId = eventId,
                    body = eventRequestEntity,
                    token = token,
                )
                response.isSuccessful && response.body() != null
            } catch (e: java.lang.Exception) {
                false
            }
        }
    }

    fun getLogBookById(logBookId: String, token: String) {
        viewModelScope.launch {

            val response = apiService.getLogBookById(
                logBookId = logBookId,
                token = token,
            )
            val result = when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        logBookMapper.map(body)
                    } else {
                        Result.failure(Exception("Body was empty"))
                    }
                }
                else -> Result.failure(Exception("Something went wrong: Code ${response.code()}"))
            }

            mutableLogBook.emit(result)
        }
    }


    fun fetchSessionsByUserId(userId: String, token: String) {
        viewModelScope.launch {

            val response = apiService.getSessionsByUserId(
                userId = userId,
                token = token,
            )

            val result = when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        sessionMapper.map(body)
                    } else {
                        Result.failure(Exception("Body was empty"))
                    }
                }
                else -> Result.failure(Exception("Something went wrong: Code ${response.code()}"))
            }

            mutableSessions.emit(result)
        }
    }

    fun fetchUsersBySessionId(sessionId: String, token: String) {
        viewModelScope.launch {
            val response = apiService.getUsersBySessionId(
                sessionId = sessionId,
                token = token,
            )
            val result = when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        userMapper.map(body)
                    } else {
                        Result.failure(Exception("Body was empty"))
                    }
                }
                else -> Result.failure(Exception("Something went wrong: Code ${response.code()}"))
            }
            mutableUsers.emit(result)
        }
    }


    //Media services API endpoints
    //Start the live streaming and save the fetched HLS url to the session
    //Live streaming calls
    fun startLiveStreamingProcess(token: String, videoId: String) {

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        CoroutineScope(IO + coroutineExceptionHandler).launch {
            val executionTime = measureTimeMillis {
                val liveEvent = async {
                    println("Debug: launching update live event on thread: ${Thread.currentThread().name}")
                    apiMediaService.updateLiveEvent(
                        body = LiveEventEntity(
                            StopLiveBool = false
                        ),
                        token = token,
                    )
                }.await()


                if (liveEvent.isSuccessful && liveEvent.body() != null) {
                    apiService.editVideoDetails(
                        body = VideoDetailsEntity(
                            VideoId = videoId, VideoURL = liveEvent.body()!!.HLS
                        ),
                        token = token,
                    )

                    HLS.value = liveEvent.body()!!.HLS
                }
            }
            println("Debug: total elapsed time: $executionTime ms.")
        }
    }


    fun stopLiveEvent(token: String) {
        viewModelScope.launch {
            succeeded = try {
                val response = apiMediaService.updateLiveEvent(
                    body = LiveEventEntity(
                        StopLiveBool = true
                    ),
                    token = token,
                )
                response.isSuccessful
            } catch (e: java.lang.Exception) {
                false
            }
        }
    }

    //Stopwatch
    private var time: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds by mutableStateOf("00")
    var minutes by mutableStateOf("00")
    var hours by mutableStateOf("00")
    var isPlaying by mutableStateOf(false)

    fun start() {
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            time = time.plus(1.seconds)
            updateTimeStates()
        }
        isPlaying = true
    }

    fun pause() {
        timer.cancel()
        isPlaying = false
    }

    fun stop() {
        pause()
        time = Duration.ZERO
        updateTimeStates()
    }

    private fun updateTimeStates() {
        time.toComponents { hours, minutes, seconds, _ ->
            this@SessionViewModel.seconds = seconds.pad()
            this@SessionViewModel.minutes = minutes.pad()
            this@SessionViewModel.hours = hours.padHours()
        }
        taskTimer()
    }


    private fun taskTimer() {
        altitude = (10000..50000).random()
        if (generatedEvents <= maxNumberOfEvents) {
            if (secondsPassed % cycleTimeInSeconds == 0) {
                if (!token.value.isEmpty()) {
                    generateEvent(token.value)
                }
            }
        }
        secondsPassed += 1
    }


    private fun generateEvent(token: String) {


        if (sessionProperties != null && generatedEvents <= maxNumberOfEvents) {

            var timeStamp = secondsPassed

            if (generatedEvents < maxNumberOfEvents) {
                if (generatedEvents == 0) {
                    createEvent(
                        eventRequestEntity = getEventRequestEntity(
                            EventType.TAKE_OFF.name, timeStamp = timeStamp
                        ), token = token
                    )
                } else {
                    val randomEventIndex = (1..6).random()
                    createEvent(
                        eventRequestEntity = getEventRequestEntity(
                            EventType.values()[randomEventIndex].name, timeStamp = timeStamp
                        ), token = token
                    )
                }
            }

            if (generatedEvents == maxNumberOfEvents) {
                createEvent(
                    eventRequestEntity = getEventRequestEntity(
                        EventType.LANDING.name, timeStamp = timeStamp
                    ), token = token
                )
            }

            //Create a record
            createRecord(
                recordEntity = RecordEntity(
                    timeStamp = timeStamp,
                    altitude = altitude,
                    logbookId = sessionProperties!!.logbookId
                ), token = token
            )

            //Get the new list
            getLogBookById(
                logBookId = sessionProperties!!.logbookId, token = token
            )
        }
        generatedEvents += 1
    }

    private fun getEventRequestEntity(eventType: String, timeStamp: Int): EventRequestEntity {
        return EventRequestEntity(
            timeStamp = timeStamp,
            eventType = eventType,
            logbookId = sessionProperties!!.logbookId,
            captain = null,
            firstOfficer = null,
            feedbackAll = null,
            feedbackCaptain = null,
            feedbackFirstOfficer = null,
            ratingAll = null,
            ratingCaptain = null,
            ratingFirstOfficer = null,
        )
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    private fun Long.padHours(): String {
        return this.toString().padStart(2, '0')
    }

    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }

    fun getRating() {
        currentRating.value = when (selectedParticipantTabIndex.value) {
            0 -> {
                selectedEvent.value.ratingFirstOfficer
                selectedEvent.value.ratingFirstOfficer?.let { selectedEvent.value.ratingFirstOfficer }
                    ?: 0
            }
            1 -> {
                selectedEvent.value.ratingAll?.let { selectedEvent.value.ratingAll } ?: 0
            }
            else -> {
                selectedEvent.value.ratingCaptain?.let { selectedEvent.value.ratingCaptain } ?: 0
            }
        }
    }

    fun getFeedback() {
        currentFeedback.value = when (selectedParticipantTabIndex.value) {
            0 -> {
                selectedEvent.value.feedbackFirstOfficer?.let { selectedEvent.value.feedbackFirstOfficer }
                    ?: ""
            }
            1 -> {
                selectedEvent.value.feedbackAll?.let { selectedEvent.value.feedbackAll } ?: ""
            }
            else -> {
                selectedEvent.value.feedbackCaptain?.let { selectedEvent.value.feedbackCaptain }
                    ?: ""
            }
        }
    }

    fun hasFeedback(index: Int): Boolean {
        return when (index) {
            0 -> {
                !selectedEvent.value.feedbackFirstOfficer.isNullOrEmpty()
            }
            1 -> {
                !selectedEvent.value.feedbackAll.isNullOrEmpty()
            }
            else -> {
                !selectedEvent.value.feedbackCaptain.isNullOrEmpty()
            }
        }
    }

    fun saveChanges(token: String) {
        savingChanges = true
        val eventRequestEntity = createEventRequestEntity()

        //Check if it is a new mark event
        if (isMarkEvent()) {
            //If it is a new event call the create event endpoint

            if (eventRequestEntity != null) {
                createEvent(
                    eventRequestEntity = eventRequestEntity, token = token
                )
            }

        } else {
            //If it is an exiting event call the update event endpoint
            if (eventRequestEntity != null) {
                updateEvent(
                    eventId = selectedEvent.value.id,
                    eventRequestEntity = eventRequestEntity,
                    token = token
                )
            }
        }

        if (eventRequestEntity != null) {
            getLogBookById(
                logBookId = eventRequestEntity.logbookId, token = token
            )
        }

        viewModelScope.launch {
            delay(4000)
            savingChanges = false
        }
    }

    fun isMarkEvent(): Boolean {
        return selectedEvent.value.id == EventType.MARKED_EVENT.name
    }


    private fun createEventRequestEntity(): EventRequestEntity? {
        addFeedbackAndRatingToSelectedEvent()
        return sessionProperties?.let {
            EventRequestEntity(
                timeStamp = secondsPassed,
                eventType = EventType.MARKED_EVENT.name,
                logbookId = it.logbookId,
                captain = null,
                firstOfficer = null,
                feedbackAll = selectedEvent.value.feedbackAll,
                feedbackCaptain = selectedEvent.value.feedbackCaptain,
                feedbackFirstOfficer = selectedEvent.value.feedbackFirstOfficer,
                ratingAll = selectedEvent.value.ratingAll,
                ratingCaptain = selectedEvent.value.ratingCaptain,
                ratingFirstOfficer = selectedEvent.value.ratingFirstOfficer,
            )
        }
    }

    private fun addFeedbackAndRatingToSelectedEvent() {
        //Save feedback and rating
        when (selectedParticipantTabIndex.value) {
            0 -> {
                selectedEvent.value.feedbackFirstOfficer = currentFeedback.value
                selectedEvent.value.ratingFirstOfficer = currentRating.value
            }
            1 -> {
                selectedEvent.value.feedbackAll = currentFeedback.value
                selectedEvent.value.ratingAll = currentRating.value
            }
            else -> {
                selectedEvent.value.feedbackCaptain = currentFeedback.value
                selectedEvent.value.ratingCaptain = currentRating.value
            }
        }
    }

    //Enable mark event button when not loading and the selected event is not mark event
    fun markEventButtonEnabled(): Boolean {
        return selectedEvent.value.id != EventType.MARKED_EVENT.name && !loading.value
    }

    //Add empty marked event
    fun addMarkEvent() {
        selectedEvent.value = Event(
            "MARKED_EVENT",
            "LOG_BOOK",
            0,
            EventType.MARKED_EVENT.name,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        selectedItemIndex.value = 400
    }

    //Check if the session is still running
    fun isSessionStillRunning(): Boolean {
        return selectedSession.value.status == SessionStatus.STARTED.name
    }

}