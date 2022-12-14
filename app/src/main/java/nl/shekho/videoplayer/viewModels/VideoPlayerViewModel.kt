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
    private val connectivityChecker: ConnectivityChecker,
    private val apiMediaService: ApiMediaService
) : ViewModel() {

    //Response information
    var succeeded = mutableStateOf(false)
    var failed: Boolean by mutableStateOf(false)
    var failedMessage: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(true)

    //Progress bar
    var accomplishedSteps = mutableStateOf(0)
    var currentStep = mutableStateOf("")

    //Asset and live streaming variables
    val streamingEndpoints: MutableState<List<StreamingEntity>> = mutableStateOf(ArrayList())
    var asset: Asset? by mutableStateOf(null)
    var liveEventName: String by mutableStateOf("")
    var liveEvent: LiveEventResponseEntity? by mutableStateOf(null)
    var showIngestUrl: Boolean by mutableStateOf(false)
    var liveEventStatus: LiveEventUpdateEntity? by mutableStateOf(null)
    var runningStreaming: StreamingEntity? by mutableStateOf(null)


    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())

    var token =
        "eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJST0xFX0lOU1RSVUNUT1IiLCJDb21wYW55SWQiOiJkNjg0ZGFlMy03NDJiLTQ4ZDQtYjVjMC0wYmJkMDZiYzVjM2EiLCJVc2VySWQiOiI0ZmI4ZGRjNy02NzFhLTQ0ZWMtODBhYy0zMzE2NGE1NjYxODIiLCJuYmYiOjE2NzEwMjQ0MzUsImV4cCI6MTY3MTExMDgzNSwiaWF0IjoxNjcxMDI0NDM1LCJpc3MiOiJodHRwczovL3ZyZWZzb2x1dGlvbnNkZXYwMDEuYXp1cmV3ZWJzaXRlcy5uZXQvYXBpLyIsImF1ZCI6Imh0dHBzOi8vdnJlZnNvbHV0aW9uc2RldjAwMS5henVyZXdlYnNpdGVzLm5ldC9hcGkvIn0.LMxs9b1tBUuLiYmtGkGml35uNqFICbQrl8WFSeYJbJc"

    init {
        startLiveStreamingProcess(token)
    }

    private fun startLiveStreamingProcess(token: String) {
        viewModelScope.launch {
            step1CreateEmptyAsset(token)
            step2PublishAsset(token)
            step3CreateLiveEvent(token)
        }
    }

    fun continueStreamingProcess(token: String) {
        viewModelScope.launch {
            step5UpdateLiveEvent("false", token)
            step6FetchStreamingEndpoints(token)
            step7StartStreamingPlatform()
            loading = false
        }
    }

    private suspend fun step1CreateEmptyAsset(token: String) {

        if (!isOnline()) {
            terminateLiveStreamingProcess("You have no internet connection! connect to the internet and relaunch the app!")
        } else {
            try {

                currentStep.value = LiveStreamingSetup.CREATEASSET.type
                accomplishedSteps.value = 1

                val response = apiMediaService.createEmptyAsset(
                    body = AssetRequestBodyEntity(
                        assetName = "instructor"
                    ),
                    token = "Bearer $token"
                )

                if (response.isSuccessful && response.body() != null) {
                    asset = response.body()
                } else {
                    terminateLiveStreamingProcess("Step 1 went wrong: ${response.errorBody()}")
                }
            } catch (e: java.lang.Exception) {
                terminateLiveStreamingProcess("Something went wrong in step 1: ${e.message}")
            }
        }
//        currentStep.value = LiveStreamingSetup.CREATEASSET.type
//        accomplishedSteps.value = 1
        delay(3000)
    }


    private suspend fun step2PublishAsset(token: String) {
        try {
            currentStep.value = LiveStreamingSetup.PUBLISHASSET.type
            accomplishedSteps.value = 2

            val response = apiMediaService.publishAsset(
                body = AssetRequestPublishedEntity(
                    AssetName = asset!!.assetName
                ),
                token = "Bearer $token"
            )

            if (!response.isSuccessful) {
                terminateLiveStreamingProcess("Step 2 went wrong: ${response.errorBody()}")
            }
        } catch (e: java.lang.Exception) {
            terminateLiveStreamingProcess("Something went wrong in step 2: ${e.message}")
        }

//        currentStep.value = LiveStreamingSetup.PUBLISHASSET.type
//        accomplishedSteps.value = 2
        delay(3000)
    }

    private suspend fun step3CreateLiveEvent(token: String) {

        try {
            currentStep.value = LiveStreamingSetup.CREATELIVEEVENT.type
            accomplishedSteps.value = 3

            val liveEventNameCreated =
                "instructorLiveEvent${LocalDateTime.now().dayOfWeek}${LocalDateTime.now().hour}-${LocalDateTime.now().minute}"
            liveEventName = liveEventNameCreated
            val response = apiMediaService.createLiveEvent(
                body = LiveEventRequestEntity(
                    liveEventName = liveEventName,
                    LiveOutputAssetName = asset!!.assetName
                ),
                token = "Bearer $token"
            )

            delay(3000)

            if (response.isSuccessful && response.body() != null) {
                liveEvent = response.body()
                step5SetupLiveStreamingPlatform()
            } else {
                terminateLiveStreamingProcess("Step 3 went wrong: ${response.errorBody()}")
            }
        } catch (e: java.lang.Exception) {
            terminateLiveStreamingProcess("Something went wrong in step 3: ${e.message}")
        }
    }

    private fun step5SetupLiveStreamingPlatform(){
        currentStep.value = LiveStreamingSetup.SETUPSTREAMINGPLATFORM.type
        accomplishedSteps.value = 4
        showIngestUrl = true
    }


    private suspend fun step5UpdateLiveEvent(stopLiveBool: String, token: String) {

        try {
            currentStep.value = LiveStreamingSetup.STARTLIVEEVENT.type
            accomplishedSteps.value = 5

            val response = apiMediaService.updateLiveEvent(
                body = LiveEventUpdateEntity(
                    LiveEventName = liveEventName,
                    StopLiveBool = stopLiveBool
                ),
                token = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.StopLiveBool == LiveStreamingStatus.RUNNING.type) {
                    liveEventStatus = response.body()
                } else {
                    terminateLiveStreamingProcess("Can't start the live streaming!")
                }

            } else {
                terminateLiveStreamingProcess("Step 5 went wrong: ${response.errorBody()}")
            }
        } catch (e: java.lang.Exception) {
            terminateLiveStreamingProcess("Something went wrong 5: ${e.message}")
        }
        delay(3000)
    }

    private suspend fun step6FetchStreamingEndpoints(token: String) {

        try {
            currentStep.value = LiveStreamingSetup.FETCHLIVESTREAMINGURL.type
            accomplishedSteps.value = 6

            val response = apiMediaService.fetchStreamingEndpoints(
                token = "Bearer $token"
            )

            if (response.isSuccessful && response.body() != null) {
                streamingEndpoints.value = response.body()!!
                runningStreaming = response.body()!!.firstOrNull { it.StreamName == liveEventName }
            } else {
                terminateLiveStreamingProcess("Step 6 went wrong: ${response.errorBody()}")
            }
        } catch (e: java.lang.Exception) {
            terminateLiveStreamingProcess("Something went wrong 6: ${e.message}")
        }
        delay(3000)
    }

    private suspend fun step7StartStreamingPlatform() {
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), emptyList())

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