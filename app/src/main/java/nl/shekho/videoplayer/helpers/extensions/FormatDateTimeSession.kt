package nl.shekho.videoplayer.helpers.extensions

import nl.shekho.videoplayer.models.Session
import nl.shekho.videoplayer.models.SessionStatus
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
            return "Session - ${
                formattedDate.dayOfWeek.toString().lowercase().subSequence(0, 3)
            } ${formattedDate.dayOfMonth}th-${formattedDate.hour}:${formattedMinutes}"
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

            return "Session - ${
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


        fun filterSessionIfUserIsInstructor(
            sessions: List<Session>,
            isInstructor: Boolean
        ): List<Session> {

            if (isInstructor) {
                val sessionsForInstructor = sessions.filter { session ->
                    isNotOlderThan24Hours(session.startTime) && (session.status == SessionStatus.STARTED.type || session.status == SessionStatus.FINISHED.type)
                }

                return sessionsForInstructor
            } else {
                val sessionsForPilot = sessions.filter { session ->
                    session.status == SessionStatus.FINISHED.type
                }
                return sessionsForPilot
            }

        }

        private fun isNotOlderThan24Hours(date: String): Boolean {
            val formattedDate = convertStringToLocalDateTime(date)
            val oneDayOld = LocalDateTime.now().minusHours(24)
            return formattedDate.isAfter(oneDayOld) || formattedDate.isEqual(oneDayOld)
        }

        fun convertSecondsToTime(NumberOfSeconds: Int): String{

            val hours = NumberOfSeconds / 3600;
            val minutes = (NumberOfSeconds % 3600) / 60;
            val seconds = NumberOfSeconds % 60;

            return hours.pad() + " : " + minutes.pad() + " : " + seconds.pad();
        }

        private fun Int.pad(): String {
            return this.toString().padStart(2, '0')
        }

    }
}
