package nl.shekho.videoplayer.viewModels

import android.net.Uri
import android.system.Os.link
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._nl_shekho_videoplayer_viewModels_VideoPlayerViewModel_HiltModules_BindsModule
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


@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val metaDataReader: MetaDataReader,
    private val connectivityChecker: ConnectivityChecker
) : ViewModel() {

    //Response information
    var succeeded = mutableStateOf(false)
    var failed: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    //Progress bar
    var accomplishedSteps = mutableStateOf(0)
    var currentStep = mutableStateOf("")

    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())

    init {
        viewModelScope.launch {
            loading = true
            step1CreateEmptyAsset()
            step2PublishAsset()
            step3CreateLiveEvent()
            step4SetupStreamingPlatform()
            step5StartLiveEvent()
            step5StartLiveEvent()
            step6FetchLiveStreamingUrl()
            step7StartStreamingPlatform()
            loading = false
        }
    }

    suspend fun step1CreateEmptyAsset() {
        delay(3000)
        currentStep.value = LiveStreamingSetup.CREATEASSET.type
        accomplishedSteps.value = 1
    }
    suspend fun step2PublishAsset() {
        delay(3000)
        currentStep.value = LiveStreamingSetup.PUBLISHASSET.type
        accomplishedSteps.value = 2
    }
    suspend fun step3CreateLiveEvent() {
        delay(3000)
        currentStep.value = LiveStreamingSetup.CREATELIVEEVENT.type
        accomplishedSteps.value = 3
    }
    suspend fun step4SetupStreamingPlatform() {
        delay(3000)
        currentStep.value = LiveStreamingSetup.SETUPSTREAMINGPLATFORM.type
        accomplishedSteps.value = 4
    }
    suspend fun step5StartLiveEvent() {
        delay(3000)
        currentStep.value = LiveStreamingSetup.STARTLIVEEVENT.type
        accomplishedSteps.value = 5
    }
    suspend fun step6FetchLiveStreamingUrl() {
        delay(3000)
        currentStep.value = LiveStreamingSetup.FETCHLIVESTREAMINGURL.type
        accomplishedSteps.value = 6
    }
    suspend fun step7StartStreamingPlatform() {
        delay(3000)
        currentStep.value = LiveStreamingSetup.STARTSTREAMINGPLATFORM.type
        accomplishedSteps.value = 7
    }


    val videoItems = videoUris.map { uris ->
        uris.map { uri ->
            VideoItem(
                contentUri = uri,
                mediaItem = MediaItem.fromUri(uri),
                name = metaDataReader.getMetaDataFromUri(uri)?.fileName ?: "No name"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        player.prepare()
        addMockVideo()
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

    fun addMockVideo() {
        var videoURL =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        val videoURI: Uri = Uri.parse(videoURL)
        addVideoUri(videoURI)
        playVideo(videoURI)
        player.prepare()
        player.playWhenReady = true
    }
}