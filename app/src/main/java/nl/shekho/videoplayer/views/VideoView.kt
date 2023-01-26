package nl.shekho.videoplayer.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ui.PlayerView
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun VideoView(
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel
) {

    var testVideo = stringResource(id = R.string.testVideo)

    var lifeCycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifeCycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent, RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AndroidView(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .fillMaxSize(),
                    factory = { context ->
                        PlayerView(context).also {
                            it.player = sessionViewModel.player
                        }
                    },
                    update = {
                        when (lifeCycle) {
                            Lifecycle.Event.ON_PAUSE -> {
                                it.onPause()
                                it.player?.pause()
                            }
                            Lifecycle.Event.ON_RESUME -> {
                                it.onResume()
                            }
                            else -> Unit
                        }
                    }
                )
            }

            //Video loading state
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                if ((sessionViewModel.liveStreamingLoading && !sessionViewModel.liveStreamingPlaying) || sessionViewModel.liveStreamingSettingUp) {
                    CircularProgressIndicator()
                }
            }

            //Start the live streaming - out commented because it is only needed for the live streaming
//            Box(
//                contentAlignment = Alignment.TopEnd,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Transparent)
//            ) {
//                if (accessViewModel.userIsInstructor.value) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_baseline_refresh_24),
//                        contentDescription = "",
//                        tint = MaterialTheme.colors.primary,
//                        modifier = Modifier
//                            .size(40.dp)
//                            .padding(6.dp)
//                            .clickable {
//                                if (!sessionViewModel.player.isPlaying && !sessionViewModel.player.isLoading) {
//
//                                    if (sessionViewModel.HLS.value == testVideo) {
//                                        sessionViewModel.fetchVideoFromUrl(testVideo)
//                                    } else {
//                                        sessionViewModel.startLiveStreaming(sessionViewModel.HLS.value)
//                                    }
//                                }
//                            }
//                    )
//                }
//            }
        }
    }
}
