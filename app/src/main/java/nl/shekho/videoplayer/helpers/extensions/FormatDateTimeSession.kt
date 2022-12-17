package nl.shekho.videoplayer.helpers.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Helpers{
    companion object{
        fun formatDateTimeSession(date: String): String?{
            var stringDate = date
            stringDate = stringDate.replaceAfter(delimiter = ".", "")
            stringDate = stringDate.replace("T", " ")
            stringDate = stringDate.replace(".", "")

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDate = LocalDateTime.parse(stringDate, formatter)


            val formattedMinutes = String.format("%02d", formattedDate.minute)
            return "Session - ${formattedDate.dayOfWeek.toString().lowercase().subSequence(0, 3)} ${formattedDate.dayOfMonth}th-${formattedDate.hour}:${formattedMinutes}"
        }

        private fun extractSanitizedStringFromDate(date: String): String{
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

            return "Session - ${formattedDate.dayOfWeek.toString().lowercase()} ${formattedDate.dayOfMonth}th"
        }

        fun getSessionDate(date: String): String {
            val sanitizedDate = extractSanitizedStringFromDate(date)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDate = LocalDateTime.parse(sanitizedDate, formatter)

            return formattedDate.format(formatter)
        }

        fun getSessionName(): String{
            val date = LocalDateTime.now()
            val formattedMinutes = String.format("%02d", date.minute)
            return "Session - ${
                date.dayOfWeek.toString().lowercase().subSequence(0, 3)
            } ${date.dayOfMonth}th - ${date.hour}:${formattedMinutes}"
        }
    }
}
