package nl.shekho.videoplayer.views

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import nl.shekho.videoplayer.viewModels.VideoPlayerViewModel
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun VideoView(
    sessionViewModel: SessionViewModel,
    videoPlayerViewModel: VideoPlayerViewModel
){

    val videoItems by videoPlayerViewModel.videoItems.collectAsState()
    val selectVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let(videoPlayerViewModel::addVideoUri)
        }
    )

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

    if(videoPlayerViewModel.loading || sessionViewModel.updatingLiveEvent){
        CircularProgressIndicator()
    }else{

        if(videoPlayerViewModel.failed){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ){
                Text(
                    text = videoPlayerViewModel.failedMessage,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }else{
            Column(
                modifier = Modifier
                    .background(color = Color.Transparent, RoundedCornerShape(20.dp))
            ) {

                AndroidView(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .fillMaxSize(),
                    factory = { context ->
                        PlayerView(context).also {
                            it.player = videoPlayerViewModel.player
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
                    },
                )
            }
        }
    }
}
