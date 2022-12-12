package nl.shekho.videoplayer.viewModels

import android.net.Uri
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
import nl.shekho.videoplayer.api.ApiService
import nl.shekho.videoplayer.api.entities.AssetRequestBodyEntity
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.MetaDataReader
import nl.shekho.videoplayer.models.LiveStreamingSetup
import nl.shekho.videoplayer.models.VideoItem
import javax.inject.Inject
import nl.shekho.videoplayer.PilotTrainingApp.Companion.globalToken
import nl.shekho.videoplayer.api.ApiMediaService
import nl.shekho.videoplayer.api.entities.SessionEntity
import nl.shekho.videoplayer.models.Asset
import java.util.UUID


@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val metaDataReader: MetaDataReader,
    private val connectivityChecker: ConnectivityChecker,
    private val apiMediaService: ApiMediaService
) : ViewModel() {

    //Response information
    var succeeded = mutableStateOf(false)
    var failed: Boolean by mutableStateOf(false)
    var failedMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)

    //Progress bar
    var accomplishedSteps = mutableStateOf(0)
    var currentStep = mutableStateOf("")

    //Asset and live streaming variables
    var asset: Asset? by mutableStateOf(null)


    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())

    init {
        startLiveStreamingProcess()
    }

    private fun startLiveStreamingProcess(){
        player.prepare()
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

        if(!isOnline()){
            terminateLiveStreamingProcess("You have no internet connection! connect to the internet and relaunch the app!")
        }else{
            try {

                val response = apiMediaService.createEmptyAsset(
                    body = AssetRequestBodyEntity(
                        ContainerName = "container",
                        assetName = "instructor"
                    ),
                    token = "Bearer $globalToken"
                )
                print(response)

                if (response.isSuccessful) {
                    val body = response.body()
                    currentStep.value = LiveStreamingSetup.CREATEASSET.type
                    accomplishedSteps.value = 1
                    asset = body

                } else {
                    terminateLiveStreamingProcess("Something went wrong!")
                }
            } catch (e: java.lang.Exception) {
                terminateLiveStreamingProcess("Something went wrong!")
            }
        }
        delay(3000)
    }


    suspend fun step2PublishAsset() {
        delay(3000)
        failed = true
        loading = false
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

    private fun terminateLiveStreamingProcess(message: String){
        failed = true
        loading = false
        failedMessage = message
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

    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }
}