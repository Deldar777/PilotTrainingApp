package nl.shekho.videoplayer.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.api.ApiService
import nl.shekho.videoplayer.api.SessionMapper
import nl.shekho.videoplayer.api.UserMapper
import nl.shekho.videoplayer.api.entities.NewSessionEntity
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.models.*
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
) : ViewModel() {

    // Modify and read feedback and rating
    var selectedParticipantTabIndex: MutableState<Int> = mutableStateOf(1)
    var currentRating: MutableState<Int> = mutableStateOf(0)
    var currentFeedback: MutableState<String> = mutableStateOf("")
    var saveChangesAsked: Boolean by mutableStateOf(false)
    var saveChangesSucceeded: Boolean by mutableStateOf(false)


    //Alert dialog
    var openDialog = mutableStateOf(false)
    var savingSession: Boolean by mutableStateOf(false)
    var saveSessionAsked: Boolean by mutableStateOf(false)

    //Events automation
    var secondsPassed: Int by mutableStateOf(0)
    var maxNumberOfEvents = 20
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
        mutableStateOf(Session(null, LocalDateTime.now().toString(), null, null, null))
    var runningSession: Session? by mutableStateOf(null)
    var selectedSessionIndex = mutableStateOf(100)

    //Events
    private val mutableEvents = MutableStateFlow<Result<List<Event>>?>(null)
    var selectedEvent =
        mutableStateOf(
            Event(
                null,
                EventType.MARKEDEVENT,
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
    var events: List<Event?> by mutableStateOf(mutableListOf())
    var selectedItemIndex = mutableStateOf(100)
    var altitude: Int by mutableStateOf(0)

    init {
        getEvents()
    }

    fun resetViewWindowsValues() {
        showNewSessionWindow.value = false
        showReviewWindow.value = false
        showEmptyReview.value = true
        runningSession = null
    }

    fun endSession(sessionId: String, token: String) {

        viewModelScope.launch {
            savingSession = true

            try {
                succeeded = true
//                val response = apiService.updateSessionStatusById(
//                    sessionId = sessionId,
//                    token = token,
//                )
//
//                if (response.isSuccessful) {
//                    succeeded = true
//                } else {
//                    failed = response.message()
//                }

                delay(4000)
            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
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
                } else {
                    failed = response.message()
                }
            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }
            loading.value = false
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
                generateEvent()
            }
        }
    }

    fun getEvents() {
        events = events + listOf(
            Event(
                UUID.randomUUID().toString(),
                EventType.TAKEOFF,
                LocalDateTime.now().minusMinutes(80).toString(),
                altitude = (10000..50000).random(), null, null, null, null, null, null
            ),
            Event(
                UUID.randomUUID().toString(),
                EventType.ENGINEFAILURE,
                LocalDateTime.now().minusMinutes(60).toString(),
                altitude = (10000..50000).random(),
                "Good communication",
                "More attention",
                null,
                5,
                3,
                4
            ),
            Event(
                UUID.randomUUID().toString(),
                EventType.MARKEDEVENT,
                LocalDateTime.now().minusMinutes(40).toString(),
                altitude = (10000..50000).random(), null, null, null, 4, null, null
            ),
            Event(
                UUID.randomUUID().toString(),
                EventType.MASTERWARNING,
                LocalDateTime.now().minusMinutes(15).toString(),
                altitude = (10000..50000).random(), null, null, null, null, null, null
            ),
            Event(
                UUID.randomUUID().toString(),
                EventType.ENGINEFIRE,
                LocalDateTime.now().minusMinutes(8).toString(),
                altitude = (10000..50000).random(), null, null, "Good reaction", null, null, 4
            ),
            Event(
                UUID.randomUUID().toString(),
                EventType.TCAS,
                LocalDateTime.now().minusMinutes(4).toString(),
                altitude = (10000..50000).random(), null, null, null, 1, null, null
            ),
            Event(
                UUID.randomUUID().toString(),
                EventType.LANDING,
                LocalDateTime.now().minusMinutes(1).toString(),
                altitude = (10000..50000).random(), null, null, null, 2, null, null
            )
        )
    }

    private fun generateEvent() {

        if (generatedEvents == 0) {
            events = events + listOf(
                Event(
                    UUID.randomUUID().toString(),
                    EventType.TAKEOFF,
                    LocalDateTime.now().toString(),
                    altitude, null, null, null, null, null, null
                )
            )
        } else {
            val randomEventIndex = (1..6).random()
            events = events + listOf(
                Event(
                    UUID.randomUUID().toString(),
                    EventType.values()[randomEventIndex],
                    LocalDateTime.now().toString(),
                    altitude, null, null, null, null, null, null
                )
            )
        }

        if (generatedEvents == maxNumberOfEvents) {
            events = events + listOf(
                Event(
                    UUID.randomUUID().toString(),
                    EventType.LANDING,
                    LocalDateTime.now().toString(),
                    altitude, null, null, null, null, null, null
                )
            )
        }

        generatedEvents += 1
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

    fun saveChanges() {

        //Start with the process of saving the changes
        saveChangesAsked = true

        viewModelScope.launch {
            loading.value = true
            delay(5000)

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


            //Give feedback on the performed action
            loading.value = false
            saveChangesSucceeded = true
        }

    }
}