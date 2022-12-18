package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.views.topbarCells.*

@OptIn(ExperimentalTime::class)
@Composable
fun TopBarSession(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel,
    navController: NavController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(customDarkGray)
            .height(75.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.vref_logo_short_version),
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxSize()
                .background(customDarkGray)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                SessionTopBarFirstSection(
                    accessViewModel = accessViewModel,
                    sessionViewModel = sessionViewModel,
                    titleColor = lightBlue,
                    iconsColor = lightBlue
                )
            }

            // Second section
            Box(
                modifier = Modifier
                    .weight(1.5f)
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
                    .weight(0.75f)
                    .background(customDarkGray)
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                SessionTopBarThirdSection(
                    sessionViewModel = sessionViewModel,
                    navController = navController
                )
            }
        }
    }
}

