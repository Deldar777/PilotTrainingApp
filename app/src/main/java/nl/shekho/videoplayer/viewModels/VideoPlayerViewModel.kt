package nl.shekho.videoplayer.viewModels

import android.media.session.PlaybackState
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlaybackException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.MetaDataReader
import nl.shekho.videoplayer.models.VideoItem
import javax.inject.Inject


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
        addPlayerListeners()
        player.prepare()
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


    fun fetchVideoFromUrl(videoUrl: String){
        val videoURI: Uri = Uri.parse(videoUrl)
        addVideoUri(videoURI)
        playVideo(videoURI)
        player.prepare()
        player.playWhenReady = true
    }

    fun fetchLiveStreaming(videoUrl: String){
        loading = true
        val videoURI: Uri = Uri.parse(videoUrl)

        addVideoUri(videoURI)
        playVideo(videoURI)
        player.prepare()
        player.playWhenReady = true
    }

    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }


    //Add event listener to the player to update loading status
    private fun addPlayerListeners() {
        player.addListener(object : Player.Listener{
            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                loading = isLoading
            }
        })
    }
}