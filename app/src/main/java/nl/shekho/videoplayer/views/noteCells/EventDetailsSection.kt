package nl.shekho.videoplayer.views.noteCells

import android.content.Context
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
import nl.shekho.videoplayer.helpers.extensions.Helpers
import nl.shekho.videoplayer.models.EventType
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.SavingSessionFeedback
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun EventDetailsSection(
    sessionViewModel: SessionViewModel,
    title: String,
    subTitle: String,
    context: Context
) {

    val logBook by sessionViewModel.logBook.collectAsState()

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
            fontSize = 16.sp,
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
                fontSize = 14.sp,
            )

            Icon(
                painter = painterResource(id = Helpers.getEventIcon(sessionViewModel.selectedEvent.value.eventType)),
                contentDescription = "",
                tint = Color(Helpers.getEventIconColor(sessionViewModel.selectedEvent.value.eventType)),
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = Helpers.getEventName(sessionViewModel.selectedEvent.value.eventType),
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
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
                fontSize = 14.sp,
            )

            logBook?.let { listEvents ->
                listEvents
                    .onSuccess {
                        Text(
                            text = if (sessionViewModel.selectedEvent.value.eventType == EventType.MARKED_EVENT.name &&
                                sessionViewModel.selectedEvent.value.id == EventType.MARKED_EVENT.name
                            ) stringResource(
                                id = R.string.currentAltitude
                            ) else "${
                                Helpers.getAltitude(
                                    it.records,
                                    timeStamp = sessionViewModel.selectedEvent.value.timeStamp
                                )
                            } ft",
                            color = MaterialTheme.colors.secondary,
                            fontSize = 16.sp,
                        )
                    }
                    .onFailure {

                    }
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.timestamp)}: ",
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp,
            )
            Text(
                text = if (sessionViewModel.selectedEvent.value.id == EventType.MARKED_EVENT.name) {
                    stringResource(id = R.string.currentTimestamp)} else{ Helpers.convertSecondsToTime(
                    sessionViewModel.selectedEvent.value.timeStamp)},
                color = MaterialTheme.colors.secondary,
                fontSize = 14.sp,
            )
        }

        //Feedback on performed action
        SavingSessionFeedback(
            sessionViewModel = sessionViewModel,
            context = context
        )
    }
}

