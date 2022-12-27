package nl.shekho.videoplayer.models

import android.graphics.Color
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.orange

class Event(
    var eventType: EventType,
    var timestamp: String?,
    var altitude: Int?,
    var feedbackAll: String?,
    var feedbackFirstOfficer: String?,
    var feedbackCaptain: String?,
    var ratingAll: Int?,
    var ratingFirstOfficer: Int?,
    var ratingCaptain: Int?,
) {

    val feedbackIcon: Int = R.drawable.feedback_logo

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