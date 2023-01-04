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
                painter = painterResource(id = sessionViewModel.getEventIcon(sessionViewModel.selectedEvent.value.eventType)),
                contentDescription = "",
                tint = Color(sessionViewModel.getEventIconColor(sessionViewModel.selectedEvent.value.eventType)),
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = sessionViewModel.getEventName(stringResource(id = R.string.eventType)),
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
                text = "${(10000..50000).random()} ft",
                color = MaterialTheme.colors.secondary,
                fontSize = 16.sp,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.currentTimestamp),
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp,
            )
            Text(
                text = Helpers.convertSecondsToTime(sessionViewModel.selectedEvent.value.timeStamp),
                color = MaterialTheme.colors.secondary,
                fontSize = 16.sp,
            )
        }

        //Feedback on performed action
        SavingSessionFeedback(
            sessionViewModel = sessionViewModel,
            context = context
        )
    }
}
