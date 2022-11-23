package nl.shekho.videoplayer.views.noteCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.SessionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailsSection(
    sessionViewModel: SessionViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start

    ) {

        Text(
            text = stringResource(id = R.string.addNote),
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )

        Text(
            text = stringResource(id = R.string.addNoteDescription),
            color = MaterialTheme.colors.secondary,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(bottom = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.eventType)}: ",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Icon(
                painter = painterResource(id = sessionViewModel.selectedEvent.value.eventIcon),
                contentDescription = "Feedback icon",
                tint = Color(sessionViewModel.selectedEvent.value.eventIconColor),
                modifier = Modifier.size(26.dp)
            )

            Text(
                text = if (sessionViewModel.selectedEvent.value.eventType != null) sessionViewModel.selectedEvent.value.eventType!!.type else "Marked event",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }



        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.altitude)}: ",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Text(
                text = if (sessionViewModel.selectedEvent.value.altitude != null) sessionViewModel.selectedEvent.value.altitude.toString() else "Current altitude",
                color = MaterialTheme.colors.secondary,
                fontSize = 20.sp,
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.timestamp)}: ",
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
            )

            val timestamp = sessionViewModel.selectedEvent.value.timestamp?.let {
                formatDate(it)
            } ?: run {
                "Current timestamp"
            }
            Text(
                text = timestamp,
                color = MaterialTheme.colors.secondary,
                fontSize = 20.sp,
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(date: String): String {
    val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}