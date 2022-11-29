package nl.shekho.videoplayer.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {

    //Sessions
    private val mutableSessions = MutableStateFlow<Result<List<Session>>?>(null)
    val sessions: StateFlow<Result<List<Session>>?> = mutableSessions

    //Events
    private val mutableEvents = MutableStateFlow<Result<List<Event>>?>(null)
    val events: StateFlow<Result<List<Event>>?> = mutableEvents
    var selectedEvent = mutableStateOf(Event(null, null, null, null))


    fun getSessionsMockData(){
        val flightWare = Company("1", "Flightware")
        val andy = User("andy", "12345", "andy@gmail.com","Andy", "Henson", Role.INSTRUCTOR, flightWare)
        val daan = User("daan", "12345", "daan@gmail.com","Daan" ,"Baer", Role.FIRSTOFFICER, flightWare)
        val lisa = User("lisa", "12345", "lisa@gmail.com","Lisa" ,"Bakke", Role.CAPTAIN, flightWare)
        val users = listOf(andy,daan,lisa)
        val sessions = listOf(
            Session("1", LocalDateTime.now().minusHours(2),  users, flightWare, null),
            Session("1", LocalDateTime.now().minusHours(1),  users, flightWare, null),
            Session("1", LocalDateTime.now(),  users, flightWare, null),
        )

        viewModelScope.launch {
            delay(2000)
            val result = Result.success(sessions)
            mutableSessions.emit(result)
        }
    }

    fun getEventsMockData(){
        val events = listOf(
            Event(EventType.TAKEOFF, LocalDateTime.now().toString(), 1000, null),
            Event(EventType.MASTERWARNING, LocalDateTime.now().toString(), 4343, "Good job"),
            Event(EventType.ENGINEFAILURE, LocalDateTime.now().toString(), 434, null),
            Event(EventType.LANDING, LocalDateTime.now().toString(), 7676, "More attention"),
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