package nl.shekho.videoplayer.api.entities

data class VideoRequestEntity(
    var videoUrl: String = "",
    var sessionId: String,
    var videoContainerName: String = ""
)
