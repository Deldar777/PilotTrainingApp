package nl.shekho.videoplayer.models

import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.orange
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class Event(
    var eventType: EventType,
    var timestamp: LocalTime,
    var altitude: Int,
    var feedback: String?
) {

    @RequiresApi(Build.VERSION_CODES.O)
    companion object{
        fun getEventListMockData(): List<Event> {
            return listOf(
                Event(EventType.TAKEOFF, LocalTime.now(), 1000, null),
                Event(EventType.MASTERWARNING, LocalTime.now(), 1000, "Good job"),
                Event(EventType.ENGINEFAILURE, LocalTime.now(), 1000, null),
                Event(EventType.LANDING, LocalTime.now(), 1000, "More attention"),
            )
        }
    }


    val hasFeedback: Boolean
        get() = feedback?.isNotEmpty() == true

    val eventIcon: Int
        get() = when {
            eventType == EventType.TAKEOFF -> R.drawable.takeoff_logo
            eventType == EventType.MASTERWARNING || eventType == EventType.MASTERCAUTION -> R.drawable.warning_logo
            eventType == EventType.ENGINEFIRE -> R.drawable.local_fire_department_black_24dp
            eventType == EventType.ENGINEFAILURE -> R.drawable.failure_logo
            eventType == EventType.LANDING -> R.drawable.landing_logo
            else -> R.drawable.marked_event_logo
        }

    val eventIconColor: Int
        get() = when {
            eventType == EventType.TAKEOFF || eventType == EventType.LANDING -> deepBlue.hashCode()
            eventType == EventType.MASTERCAUTION -> Color.YELLOW
            eventType == EventType.MASTERWARNING || eventType == EventType.TCAS || eventType == EventType.ENGINEFIRE -> Color.RED
            eventType == EventType.ENGINEFAILURE -> orange.hashCode()
            else -> deepPurple.hashCode()
        }
}