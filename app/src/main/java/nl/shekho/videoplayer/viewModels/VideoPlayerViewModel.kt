package nl.shekho.videoplayer.viewModels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.MetaDataReader
import nl.shekho.videoplayer.models.LiveStreamingSetup
import nl.shekho.videoplayer.models.VideoItem
import javax.inject.Inject
import nl.shekho.videoplayer.api.ApiMediaService
import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.models.Asset
import nl.shekho.videoplayer.models.LiveStreamingStatus
import java.time.LocalDateTime


@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val metaDataReader: MetaDataReader,
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {

    //Response information
    var succeeded = mutableStateOf(false)
    var failed: Boolean by mutableStateOf(false)
    var failedMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)


    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())

    val videoItems = videoUris.map {uris ->
        uris.map {uri ->
            VideoItem(
                contentUri = uri,
                mediaItem = MediaItem.fromUri(uri),
                name = metaDataReader.getMetaDataFromUri(uri)?.fileName ?: "No name"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loading = true
        player.prepare()
        addMockVideo()
        loading = false
    }

    fun addVideoUri(uri: Uri) {
        savedStateHandle["videoUris"] = videoUris.value + uri
        player.addMediaItem(MediaItem.fromUri(uri))
    }

    fun playVideo(uri: Uri) {
        player.setMediaItem(
            videoItems.value.find { it.contentUri == uri }?.mediaItem ?: return
        )
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun addMockVideo(){
        var videoURL =  "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

        val videoURI: Uri = Uri.parse(videoURL)
        addVideoUri(videoURI)
        playVideo(videoURI)
        player.prepare()
        player.playWhenReady = true
    }

    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }
}