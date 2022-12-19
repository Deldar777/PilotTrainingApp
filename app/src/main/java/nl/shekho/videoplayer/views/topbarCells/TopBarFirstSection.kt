package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.helpers.extensions.Helpers
import kotlin.time.ExperimentalTime
import androidx.compose.ui.graphics.*

@OptIn(ExperimentalTime::class)
@Composable
fun SessionTopBarFirstSection(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel,
    iconsColor: Color,
    titleColor: Color,
    participant1Name: String,
    participant2Name: String
){
    var sessionDate = sessionViewModel.selectedSession.value.startTime

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp)
    ) {

        //Session name
        Text(
            text = "${stringResource(id = R.string.session)} - ${sessionDate.let { Helpers.formatDateTimeSessionShort(it)} ?: kotlin.run { "Date" }} ",
            fontSize = 18.sp,
            color = titleColor,
            fontWeight = FontWeight.Bold
        )

        //Session date
        Text(
            text = sessionDate.let { Helpers.getSessionDate(sessionDate)} ?: kotlin.run { stringResource(id = R.string.sessionDate)},
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )


        //Participants
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.participants)}:",
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(end = 4.dp)
            )

            //Participant 1
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = "",
                tint = iconsColor,
                modifier = Modifier
                    .size(20.dp)
            )

            Text(
                text = participant1Name,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )

            //Participant 2
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = "",
                tint = iconsColor,
                modifier = Modifier
                    .size(20.dp)
            )

            Text(
                text = participant2Name,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold

            )
        }
    }
}
