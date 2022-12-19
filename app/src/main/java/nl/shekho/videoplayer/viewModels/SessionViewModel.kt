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
    private val mutableSessions = MutableStateFlow<Result<List<Session>>?>(null)
    val sessions: StateFlow<Result<List<Session>>?> = mutableSessions
    val selectedSession = mutableStateOf(Session(null, LocalDateTime.now().toString(), null, null))
    var runningSession: SessionEntity? by mutableStateOf(null)
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
    }


    fun createSession(newSessionEntity: NewSessionEntity, token: String) {
        viewModelScope.launch {
            loading = true
            try {
                val response = apiService.createSession(
                    body = newSessionEntity,
                    token = token,
                )

                if (response.isSuccessful && response.body() != null) {
                    succeeded = true
                    runningSession = response.body()
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


    fun getEventsMockData() {
        val events = listOf(
            Event(EventType.TAKEOFF, LocalDateTime.now().toString(), 1000, feedback = ""),
            Event(EventType.MASTERWARNING, LocalDateTime.now().toString(), 4343, "Good job"),
            Event(EventType.ENGINEFAILURE, LocalDateTime.now().toString(), 434, null),
            Event(EventType.TAKEOFF, LocalDateTime.now().toString(), 32323, null),
            Event(EventType.TAKEOFF, LocalDateTime.now().toString(), 545, null),
            Event(EventType.MASTERWARNING, LocalDateTime.now().toString(), 545, "Good job"),
            Event(EventType.ENGINEFAILURE, LocalDateTime.now().toString(), 545, null),
            Event(EventType.LANDING, LocalDateTime.now().toString(), 43434, "More attention"),
        )

        viewModelScope.launch {
            delay(4000)
            val result = Result.success(events)
            mutableEvents.emit(result)
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
                Event(EventType.values()[randomEventIndex], LocalDateTime.now().toString(), 7676, "More attention"),)
        }

        if(generatedEvents == maxNumberOfEvents){
            events = events + listOf(
                Event(EventType.LANDING, LocalDateTime.now().toString(), 1000, feedback = ""),
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