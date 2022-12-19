package nl.shekho.videoplayer.helpers.extensions

import nl.shekho.videoplayer.models.Session
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


        fun filterSessionIfUserIsInstructor(sessions: List<Session>, isInstructor: Boolean): List<Session> {

            if(isInstructor){
                val filteredArray = sessions.filter { session ->
                    isNotOlderThan24Hours(session.startTime)
                }

                return filteredArray
            }

            return sessions
        }

        private fun isNotOlderThan24Hours(date: String): Boolean {
            val formattedDate = convertStringToLocalDateTime(date)
            val oneDayOld = LocalDateTime.now().minusHours(24)
            var isNotOld = formattedDate.isAfter(oneDayOld) || formattedDate.isEqual(oneDayOld)

            return formattedDate.isAfter(oneDayOld) || formattedDate.isEqual(oneDayOld)
        }
    }
}