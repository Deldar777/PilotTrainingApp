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
import nl.shekho.videoplayer.PilotTrainingApp.Companion.globalToken
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.MetaDataReader
import nl.shekho.videoplayer.models.LiveStreamingSetup
import nl.shekho.videoplayer.models.VideoItem
import javax.inject.Inject
import nl.shekho.videoplayer.api.ApiMediaService
import nl.shekho.videoplayer.api.entities.*
import nl.shekho.videoplayer.models.Asset


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
    var liveEventName: String by mutableStateOf("")
    var liveEvent: LiveEventResponseEntity? by mutableStateOf(null)
    var showIngestUrl: Boolean by mutableStateOf(false)


    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())

    init {
        startLiveStreamingProcess()
    }

    private fun startLiveStreamingProcess() {
        viewModelScope.launch {
            loading = true
            step1CreateEmptyAsset()
            step2PublishAsset()
            step3CreateLiveEvent()
        }
    }

    fun continueStreamingProcess() {
        viewModelScope.launch {
            step5UpdateLiveEvent()
            step6FetchLiveStreamingUrl()
            step7StartStreamingPlatform()
            loading = false
        }
    }

    suspend fun step1CreateEmptyAsset() {

//        if (!isOnline()) {
//            terminateLiveStreamingProcess("You have no internet connection! connect to the internet and relaunch the app!")
//        } else {
//            try {
//                currentStep.value = LiveStreamingSetup.CREATEASSET.type
//                accomplishedSteps.value = 1
//
//                val response = apiMediaService.createEmptyAsset(
//                    body = AssetRequestBodyEntity(
//                        ContainerName = "container",
//                        assetName = "instructor"
//                    ),
//                    token = "Bearer $globalToken"
//                )
//
//                if (response.isSuccessful && response.body() != null) {
//                    asset = response.body()
//                } else {
//                    terminateLiveStreamingProcess("Step 1 went wrong!")
//                }
//            } catch (e: java.lang.Exception) {
//                terminateLiveStreamingProcess("Something went wrong!")
//            }
//        }

        currentStep.value = LiveStreamingSetup.CREATEASSET.type
        accomplishedSteps.value = 1

        var fetchedAsset = Asset(
            assetName = "instructor-8a67afeb-e472",
            assetId = "cff0bd1d-b02c-420b-85b9-425b750400a7",
            container = "asset-cff0bd1d-b02c-420b-85b9-425b750400a7"
        )
        asset = fetchedAsset
        delay(3000)
    }


    suspend fun step2PublishAsset() {
//        try {
//            currentStep.value = LiveStreamingSetup.PUBLISHASSET.type
//            accomplishedSteps.value = 2
//
//            val response = apiMediaService.publishAsset(
//                body = AssetRequestPublishedEntity(
//                    AssetName = asset!!.assetName
//                ),
//                token = "Bearer $globalToken"
//            )
//
//            if (!response.isSuccessful) {
//                terminateLiveStreamingProcess("Step 2 went wrong!")
//            }
//        } catch (e: java.lang.Exception) {
//            terminateLiveStreamingProcess("Something went wrong!")
//        }

        currentStep.value = LiveStreamingSetup.PUBLISHASSET.type
        accomplishedSteps.value = 2
        delay(3000)
    }

    suspend fun step3CreateLiveEvent() {

//        try {
//            currentStep.value = LiveStreamingSetup.CREATELIVEEVENT.type
//            accomplishedSteps.value = 3
//
//            var liveEventNameCreated = "instructorLiveEvent${LocalDateTime.now().dayOfWeek}"
//            liveEventName = liveEventNameCreated
//            val response = apiMediaService.createLiveEvent(
//                body = LiveEventRequestEntity(
//                    liveEventName = liveEventName,
//                    LiveOutputAssetName = asset!!.assetName
//                ),
//                token = "Bearer $globalToken"
//            )
//
//            if (response.isSuccessful && response.body() != null) {
//                liveEvent = response.body()
//                showIngestUrl = true
//            } else {
//                terminateLiveStreamingProcess("Step 3 went wrong!")
//            }
//        } catch (e: java.lang.Exception) {
//            terminateLiveStreamingProcess("Something went wrong!")
//        }

        currentStep.value = LiveStreamingSetup.CREATELIVEEVENT.type
        accomplishedSteps.value = 3
        delay(3000)


        var fetchedLiveEvent = LiveEventResponseEntity(
            IngestURL = "rtmp://instrcutorliveevent-msvrefsolutions002-euwe.channel.media.azure.net:1935/live/acf7b6ef8a37425fb8fc51c2d6a5a86a"
        )
        liveEvent = fetchedLiveEvent
        liveEventName = "instructorLiveEvent"


        currentStep.value = LiveStreamingSetup.SETUPSTREAMINGPLATFORM.type
        accomplishedSteps.value = 4
        showIngestUrl = true
    }


    suspend fun step5UpdateLiveEvent() {

        try {
            currentStep.value = LiveStreamingSetup.STARTLIVEEVENT.type
            accomplishedSteps.value = 5

            val response = apiMediaService.updateLiveEvent(
                body = LiveEventUpdateEntity(
                    LiveEventName = liveEventName,
                ),
                token = "Bearer $globalToken"
            )

            if (response.isSuccessful && response.body() != null) {
            } else {
                terminateLiveStreamingProcess("Step 5 went wrong!")
            }
        } catch (e: java.lang.Exception) {
            terminateLiveStreamingProcess("Something went wrong!")
        }
        delay(3000)

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

    private fun terminateLiveStreamingProcess(message: String) {
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