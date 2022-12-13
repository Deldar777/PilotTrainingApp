package nl.shekho.videoplayer.api.entities

data class AssetRequestPublishedEntity(
    var AssetName: String,
    var StreamingPolicyName: String = "Predefined_DownloadAndClearStreaming"
)
