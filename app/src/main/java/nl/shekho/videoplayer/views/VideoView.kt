package nl.shekho.videoplayer.views

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import nl.shekho.videoplayer.viewModels.VideoPlayerViewModel

@Composable
fun VideoView(){
    val videoPlayerViewModel = hiltViewModel<VideoPlayerViewModel>()
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

    Column(
        modifier = Modifier
            .background(color = Color.Transparent, RoundedCornerShape(20.dp))
    ) {

        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.onBackground, RoundedCornerShape(20.dp))
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter,
        ){
            AndroidView(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .fillMaxWidth()
                    .height(400.dp),
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


        Box(
            modifier = Modifier
                .weight(.3f)
                .background(MaterialTheme.colors.onBackground, RoundedCornerShape(20.dp))
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter,
        ){
            IconButton(onClick = {
                selectVideoLauncher.launch("video/mp4")
            }) {
                Icon(
                    imageVector = Icons.Default.FileOpen,
                    contentDescription = "Select video",
                    tint = Color.Blue

                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(videoItems) { item ->
                    Text(
                        text = item.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                videoPlayerViewModel.playVideo(item.contentUri)
                            }
                            .padding(16.dp)
                    )
                }
            }

        }
    }
}
