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
import nl.shekho.videoplayer.api.entities.SessionEntity
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
    var loading: Boolean by mutableStateOf(false)
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
    var selectedSession = mutableStateOf(Session(null, LocalDateTime.now().toString(), null, null, null))
    var runningSession: Session? by mutableStateOf(null)
    var selectedSessionIndex = mutableStateOf(100)


    //Events
    private val mutableEvents = MutableStateFlow<Result<List<Event>>?>(null)
    var selectedEvent = mutableStateOf(Event(EventType.MARKEDEVENT, null, null, ""))
    var events: List<Event?> by mutableStateOf(mutableListOf())
    var selectedItemIndex = mutableStateOf(100)
    var altitude: Int by mutableStateOf(0)

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
            loading = true
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
            loading = false
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


    private fun generateEvent() {

        if (generatedEvents == 0) {
            events = events + listOf(
                Event(EventType.TAKEOFF, LocalDateTime.now().toString(), 1000, feedback = ""),
            )
        } else {
            var randomEventIndex = (1..6).random()
            events = events + listOf(
                Event(EventType.values()[randomEventIndex], LocalDateTime.now().toString(), altitude, "More attention"),)
        }

        if(generatedEvents == maxNumberOfEvents){
            events = events + listOf(
                Event(EventType.LANDING, LocalDateTime.now().toString(), 500, feedback = ""),
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
}