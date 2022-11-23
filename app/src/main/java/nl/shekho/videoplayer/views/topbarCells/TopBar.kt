@file:OptIn(ExperimentalAnimationApi::class)

package nl.shekho.videoplayer.views.topbarCells

import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
@Composable
fun TopBar(
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
        Text(
            text = "VRef solution",
            color = deepBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding()
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

@Composable
private fun Timer(
    isPlaying: Boolean,
    seconds: String,
    minutes: String,
    hours: String,
    onStart: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.weight(2f))

        Row(
            modifier = Modifier
                .background(color = Color.Gray.copy(alpha = 0.4f), shape = RoundedCornerShape(20))
                .padding(4.dp)
        ) {
            val numberTransitionSpec: AnimatedContentScope<String>.() -> ContentTransform = {
                slideInVertically() + fadeIn() with slideOutVertically() + fadeOut() using SizeTransform(false)
            }
            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.h5) {

                AnimatedContent(targetState = hours, transitionSpec = numberTransitionSpec) {
                    Text(text = it, color = MaterialTheme.colors.primary)
                }
                Text(text = ":", color = MaterialTheme.colors.primary)
                AnimatedContent(targetState = minutes, transitionSpec = numberTransitionSpec) {
                    Text(text = it, color = MaterialTheme.colors.primary)
                }
                Text(text = ":", color = MaterialTheme.colors.primary)
                AnimatedContent(targetState = seconds, transitionSpec = numberTransitionSpec) {
                    Text(text = it, color = MaterialTheme.colors.primary)
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            AnimatedContent(targetState = isPlaying) {
                if (it) {
                    IconButton(onClick = onPause) {
                        Icon(
                            painter = painterResource(id = R.drawable.timer_icon),
                            contentDescription = "",
                            tint = Color.Red
                        )
                    }
                } else {
                    IconButton(onClick = onStart) {
                        Icon(
                            painter = painterResource(id = R.drawable.timer_icon),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                    }

                }
            }


            IconButton(onClick = onStop) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = "",
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.weight(2f))
    }

}