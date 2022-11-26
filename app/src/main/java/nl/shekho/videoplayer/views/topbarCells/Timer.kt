package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Timer(
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
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = "",
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.weight(2f))
    }

}