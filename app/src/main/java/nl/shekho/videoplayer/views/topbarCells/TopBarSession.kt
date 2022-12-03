@file:OptIn(ExperimentalAnimationApi::class)

package nl.shekho.videoplayer.views.topbarCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.views.topbarCells.*

@OptIn(ExperimentalTime::class)
@Composable
fun TopBarLogin(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(customDarkGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.vref_logo),
            contentDescription = "",
            modifier = Modifier
                .width(200.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(customDarkGray)
                .padding(15.dp)

        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "First section",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Second section
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                Timer(
                    isPlaying = sessionViewModel.isPlaying,
                    seconds = sessionViewModel.seconds,
                    minutes = sessionViewModel.minutes,
                    hours = sessionViewModel.hours,
                    onStart = { sessionViewModel.start() },
                    onStop = { sessionViewModel.stop() },
                    onPause = { sessionViewModel.pause() },
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Third section",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )

            }
        }
    }
}

