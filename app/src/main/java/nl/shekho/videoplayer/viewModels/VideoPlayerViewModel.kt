package nl.shekho.videoplayer.viewModels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.MimeTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.MetaDataReader
import javax.inject.Inject


@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    val player: Player,
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {

    //Response information
    var failed: Boolean by mutableStateOf(false)
    var failedMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    init {
        player.prepare()
    }

    fun fetchVideoFromUrl(videoUrl: String){
        addPlayerListeners()
        val videoURI: Uri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(videoURI)
        player.addMediaItem(MediaItem.fromUri(videoURI))
        player.setMediaItem(mediaItem)
        player.repeatMode = Player.REPEAT_MODE_ONE //repeating the video from start after it's over
        player.prepare()
        player.playWhenReady = true
    }


    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }


    fun startLiveStreaming(mediaUrl: String) {
        //Creating a media item of HLS Type
        val mediaItem = MediaItem.Builder()
            .setUri(mediaUrl)
            .setMimeType(MimeTypes.APPLICATION_M3U8) //m3u8 is the extension used with HLS sources
            .build()

        player.setMediaItem(mediaItem)

        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ONE //repeating the video from start after it's over
        player.playWhenReady = true
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