package nl.shekho.videoplayer.views.noteCells

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
@Composable
fun EventDetailsSection(
    sessionViewModel: SessionViewModel,
    title: String,
    subTitle: String,
    context: Context
) {

    val saveChangesFailed = stringResource(id = R.string.saveChangesFailed)
    val saveChangesSucceeded = stringResource(id = R.string.saveChangesSucceeded)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.Start

    ) {

        Text(
            text = title,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )

        Text(
            text = subTitle,
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
                fontSize = 16.sp,
            )

            Icon(
                painter = painterResource(id = sessionViewModel.selectedEvent.value.eventIcon),
                contentDescription = "",
                tint = Color(sessionViewModel.selectedEvent.value.eventIconColor),
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = sessionViewModel.selectedEvent.value.eventType.type,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
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
                fontSize = 16.sp,
            )
            Text(
                text = if (sessionViewModel.selectedEvent.value.altitude != null) sessionViewModel.selectedEvent.value.altitude.toString() else "Current altitude",
                color = MaterialTheme.colors.secondary,
                fontSize = 16.sp,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.timestamp)}: ",
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp,
            )

            val timestamp = sessionViewModel.selectedEvent.value.timestamp?.let {
                formatDate(it)
            } ?: run {
                stringResource(id = R.string.currentTimestamp)
            }
            Text(
                text = timestamp,
                color = MaterialTheme.colors.secondary,
                fontSize = 16.sp,
            )
        }

        //Feedback on performed action
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            if(sessionViewModel.loading.value){
                CircularProgressIndicator()
            }

            if (sessionViewModel.saveChangesAsked) {
                if (!sessionViewModel.loading.value) {
                    if (sessionViewModel.saveChangesSucceeded) {
                        Toast.makeText(context, saveChangesSucceeded, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, saveChangesFailed, Toast.LENGTH_LONG).show()
                    }
                }
                sessionViewModel.saveChangesAsked = false
            }
        }
    }
}


private fun formatDate(date: String): String {
    val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}