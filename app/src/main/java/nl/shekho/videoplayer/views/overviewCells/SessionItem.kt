package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.models.Session
import nl.shekho.videoplayer.ui.theme.highlightDivider
import nl.shekho.videoplayer.ui.theme.lightBlue
import java.time.*
import java.time.format.DateTimeFormatter


@Composable
fun SessionItem(
    session: Session,
    isSelected: Boolean = false,
    activeHighlightColor: Color = lightBlue,
    onItemClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()
            }
            .background(
                if (isSelected) activeHighlightColor
                else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
    ) {

        Text(
            text = "${session.startTime?.let { formatDate(it) }}",
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding()
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.padding(end = 0.dp)
            )
        }

    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(color = highlightDivider, thickness = 1.dp)
    }


}

private fun formatDate(date: String): String? {
    var stringDate = date
    stringDate = stringDate.replaceAfter(delimiter = ".", "")
    stringDate = stringDate.replace("T", " ")
    stringDate = stringDate.replace(".", "")

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDate = LocalDateTime.parse(stringDate, formatter)

//    return "Session - ${formattedDate.dayOfWeek.toString().lowercase().subSequence(0, 3)} ${formattedDate.dayOfMonth}th"

    var formattedMinutes = String.format("%02d", formattedDate.minute)
    return "Session - ${formattedDate.dayOfWeek.toString().lowercase().subSequence(0, 3)} ${formattedDate.dayOfMonth}th - ${formattedDate.hour}:${formattedMinutes}"
}