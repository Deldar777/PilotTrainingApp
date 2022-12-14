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

        fun formatDateTimeSessionShort(date: String): String?{
            var stringDate = date
            stringDate = stringDate.replaceAfter(delimiter = ".", "")
            stringDate = stringDate.replace("T", " ")
            stringDate = stringDate.replace(".", "")

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDate = LocalDateTime.parse(stringDate, formatter)

            return "Session - ${formattedDate.dayOfWeek.toString().lowercase()} ${formattedDate.dayOfMonth}th"
        }
    }
}
