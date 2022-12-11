package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.deepBlue

@Composable
fun SessionTopBarFirstSection(
    accessViewModel: AccessViewModel
){

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp)
    ) {

        //Session name
        Text(
            text = "Session - ${getSessionDateFormatted()}",
            fontSize = 18.sp,
            color = lightBlue,
            fontWeight = FontWeight.Bold
        )

        //Session date
        Text(
            text = getCurrentDateFormatted(),
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
                tint = deepBlue,
                modifier = Modifier
                    .size(20.dp)
            )

            Text(
                text = accessViewModel.participant1?.firstname ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )

            //Participant 2
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = "",
                tint = deepBlue,
                modifier = Modifier
                    .size(20.dp)
            )

            Text(
                text = accessViewModel.participant2?.firstname ?: "",
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold

            )
        }
    }
}


fun getSessionDateFormatted(): String{
    val currentDate = LocalDateTime.now()
    return "${LocalDateTime.now().dayOfWeek} ${currentDate.dayOfMonth}th"
}


fun getCurrentDateFormatted(): String{
    val currentDate = LocalDateTime.now()
    val formatterPattern = "yyyy-MM-dd HH:mm"
    val formatter = DateTimeFormatter.ofPattern(formatterPattern)
    return currentDate.format(formatter)
}