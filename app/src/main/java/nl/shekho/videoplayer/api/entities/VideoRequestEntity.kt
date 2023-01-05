package nl.shekho.videoplayer.api.entities

data class VideoRequestEntity(
    var videoUrl: String = "https://vrefsolutionsdownload.blob.core.windows.net/trainevids/OVERVIEW.mp4",
    var sessionId: String,
    var videoContainerName: String = ""
)
