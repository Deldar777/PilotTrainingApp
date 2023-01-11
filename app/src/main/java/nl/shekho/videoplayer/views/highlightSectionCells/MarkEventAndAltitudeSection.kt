package nl.shekho.videoplayer.views.highlightSectionCells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.highlightItemGray
import androidx.compose.ui.res.stringResource
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.models.EventType
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun MarkEventAndAltitudeSection(
    sessionViewModel: SessionViewModel
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedButton(
            enabled =  sessionViewModel.markEventButtonEnabled(),
            onClick = {
                sessionViewModel.addMarkEvent()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = deepPurple,
                contentColor = MaterialTheme.colors.primary,
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .width(200.dp)
                .height(50.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.feedback_logo),
                contentDescription = "",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 4.dp)
            )
            Text(
                text = stringResource(id = R.string.markEvent),
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(highlightItemGray)
        ) {
            Text(
                text = "${stringResource(id = R.string.currentAltitude)} ${sessionViewModel.altitude}",
                color = MaterialTheme.colors.primary,
            )
        }
    }
}