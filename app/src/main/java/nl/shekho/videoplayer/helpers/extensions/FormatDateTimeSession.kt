package nl.shekho.videoplayer.helpers.extensions

import android.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toLowerCase
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.models.EventType
import nl.shekho.videoplayer.models.Record
import nl.shekho.videoplayer.models.Session
import nl.shekho.videoplayer.models.SessionStatus
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.orange
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Helpers {
    companion object {
        fun formatDateTimeSession(date: String): String? {
            var stringDate = date
            stringDate = stringDate.replaceAfter(delimiter = ".", "")
            stringDate = stringDate.replace("T", " ")
            stringDate = stringDate.replace(".", "")

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDate = LocalDateTime.parse(stringDate, formatter)


            val formattedMinutes = String.format("%02d", formattedDate.minute)
            return "Session - "+
                    "${formattedDate.dayOfWeek.toString().subSequence(0, 1)}"+
                    "${formattedDate.dayOfWeek.toString().lowercase().subSequence(1, 3)} "+
                    "${formattedDate.dayOfMonth}th-${formattedDate.hour}:${formattedMinutes}"
        }

        private fun extractSanitizedStringFromDate(date: String): String {
            var stringDate = date
            stringDate = stringDate.replaceAfter(delimiter = ".", "")
            stringDate = stringDate.replace("T", " ")
            stringDate = stringDate.replace(".", "")
            return stringDate
        }

        fun formatDateTimeSessionShort(date: String): String {
            val sanitizedDate = extractSanitizedStringFromDate(date)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDate = LocalDateTime.parse(sanitizedDate, formatter)

            return "${
                formattedDate.dayOfWeek.toString().lowercase()
            } ${formattedDate.dayOfMonth}th"
        }

        fun getSessionDate(date: String): String {
            val sanitizedDate = extractSanitizedStringFromDate(date)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDate = LocalDateTime.parse(sanitizedDate, formatter)

            return formattedDate.format(formatter)
        }

        private fun convertStringToLocalDateTime(date: String): LocalDateTime {
            val sanitizedDate = extractSanitizedStringFromDate(date)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return LocalDateTime.parse(sanitizedDate, formatter)
        }

        fun getSessionName(): String {
            val date = LocalDateTime.now()
            val formattedMinutes = String.format("%02d", date.minute)
            return "Session - ${
                date.dayOfWeek.toString().lowercase().subSequence(0, 3)
            } ${date.dayOfMonth}th - ${date.hour}:${formattedMinutes}"
        }


        fun filterSessions(
            sessions: List<Session>,
            isInstructor: Boolean
        ): List<Session> {

            if (isInstructor) {
                val sessionsForInstructor = sessions.filter { session ->
                    isNotOlderThan24Hours(session.startTime) && (session.status == SessionStatus.STARTED.type || session.status == SessionStatus.FINISHED.type)
                }

                return sessionsForInstructor.sortedByDescending {
                    it.startTime
                }
            } else {
                val sessionsForPilot = sessions.filter { session ->
                    session.status == SessionStatus.FINISHED.type
                }
                return sessionsForPilot.sortedByDescending {
                    it.startTime
                }
            }

        }

        private fun isNotOlderThan24Hours(date: String): Boolean {
            val formattedDate = convertStringToLocalDateTime(date)
            val oneDayOld = LocalDateTime.now().minusHours(24)
            return formattedDate.isAfter(oneDayOld) || formattedDate.isEqual(oneDayOld)
        }

        fun convertSecondsToTime(NumberOfSeconds: Int): String {

            val hours = NumberOfSeconds / 3600;
            val minutes = (NumberOfSeconds % 3600) / 60;
            val seconds = NumberOfSeconds % 60;

            return hours.pad() + ":" + minutes.pad() + ":" + seconds.pad();
        }

        private fun Int.pad(): String {
            return this.toString().padStart(2, '0')
        }

        //To get the altitude that belongs to an event
        fun getAltitude(records: List<Record?>, timeStamp: Int): Int{
            var altitude = 0
            records.forEach { record ->
                if (record != null) {
                    if(record.timeStamp == timeStamp){
                        altitude = record.altitude
                    }
                }
            }
            return altitude
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
}
