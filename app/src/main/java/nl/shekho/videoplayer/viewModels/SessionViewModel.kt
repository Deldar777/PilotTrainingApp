package nl.shekho.videoplayer.viewModels

import android.graphics.Color
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.api.*
import nl.shekho.videoplayer.api.entities.EventRequestEntity
import nl.shekho.videoplayer.api.entities.NewSessionEntity
import nl.shekho.videoplayer.api.entities.VideoRequestEntity
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.models.*
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.orange
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
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
    private val logBookMapper: LogBookMapper
) : ViewModel() {

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
            233,
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

    fun endSession(sessionId: String, token: String) {
        saveSessionAsked = true
        savingSession = true

        viewModelScope.launch {
            savingSessionSucceeded = try {
                val response = apiService.updateSessionStatusById(
                    sessionId = sessionId,
                    token = token,
                )
                delay(5000)
                response.isSuccessful
            } catch (e: java.lang.Exception) {
                false
            }
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
                        ),
                        token = token
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
            loading.value = true
            try {
                val response = apiService.postVideo(
                    body = videoRequestEntity,
                    token = token,
                )
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    succeeded = true
                    val sessionPropertiesMapped = sessionPropertiesMapper.mapEntityToModel(body)
                    sessionProperties = sessionPropertiesMapped
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

                    //If the video entity fetched successfully then get the logbook with events
                    getLogBookById(
                        logBookId = body[0].logbookId,
                        token = token
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

    private fun updateEvent(
        eventRequestEntity: EventRequestEntity,
        token: String,
        eventId: String
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
            delay(3000)

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
        secondsPassed += 1

        altitude = (10000..50000).random()
        if (generatedEvents <= maxNumberOfEvents) {
            if (secondsPassed % cycleTimeInSeconds == 0) {
                if (!token.value.isEmpty()) {
                    generateEvent(token.value)
                }
            }
        }
    }


    private fun generateEvent(token: String) {

        if (sessionProperties != null && generatedEvents <= maxNumberOfEvents) {

            if (generatedEvents < maxNumberOfEvents) {
                if (generatedEvents == 0) {
                    createEvent(
                        eventRequestEntity = getEventRequestEntity(EventType.TAKE_OFF.name),
                        token = token
                    )
                } else {
                    val randomEventIndex = (1..6).random()
                    createEvent(
                        eventRequestEntity = getEventRequestEntity(EventType.values()[randomEventIndex].name),
                        token = token
                    )
                }
            }

            if (generatedEvents == maxNumberOfEvents) {
                createEvent(
                    eventRequestEntity = getEventRequestEntity(EventType.LANDING.name),
                    token = token
                )
            }


            getLogBookById(
                logBookId = sessionProperties!!.logbookId,
                token = token
            )
        }
        generatedEvents += 1
    }

    private fun getEventRequestEntity(eventType: String): EventRequestEntity {
        return EventRequestEntity(
            timeStamp = secondsPassed,
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
                    eventRequestEntity = eventRequestEntity,
                    token = token
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
                logBookId = eventRequestEntity.logbookId,
                token = token
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
            233,
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


    //Get the event icon based on the event name
    fun getEventIcon(eventType: String): Int {

        return when (eventType) {
            EventType.TAKE_OFF.name -> R.drawable.takeoff_logo
            EventType.MASTER_WARNING.name, EventType.MASTER_CAUTION.name -> R.drawable.warning_logo
            EventType.ENGINE_FIRE.name -> R.drawable.local_fire_department_black_24dp
            EventType.ENGINE_FAILURE.name -> R.drawable.failure_logo
            EventType.LANDING.name -> R.drawable.landing_logo
            else -> R.drawable.marked_event_logo
        }
    }

    //Get the event icon color based on the event name
    fun getEventIconColor(eventType: String): Int {
        return when (eventType) {
            EventType.TAKE_OFF.name -> deepBlue.hashCode()
            EventType.LANDING.name -> deepBlue.hashCode()
            EventType.MASTER_CAUTION.name -> Color.YELLOW
            EventType.MASTER_WARNING.name -> Color.RED
            EventType.TCAS.name -> Color.RED
            EventType.ENGINE_FIRE.name -> Color.RED
            EventType.ENGINE_FAILURE.name -> orange.hashCode()
            else -> deepPurple.hashCode()
        }
    }

    //Get the event name
    fun getEventName(eventType: String): String {
        return when (eventType) {
            EventType.TAKE_OFF.name -> EventType.TAKE_OFF.type
            EventType.LANDING.name -> EventType.LANDING.type
            EventType.MASTER_CAUTION.name -> EventType.MASTER_CAUTION.type
            EventType.MASTER_WARNING.name -> EventType.MASTER_WARNING.type
            EventType.TCAS.name -> EventType.TCAS.type
            EventType.ENGINE_FIRE.name -> EventType.ENGINE_FIRE.type
            EventType.ENGINE_FAILURE.name -> EventType.ENGINE_FAILURE.type
            else -> EventType.MARKED_EVENT.type
        }
    }
}