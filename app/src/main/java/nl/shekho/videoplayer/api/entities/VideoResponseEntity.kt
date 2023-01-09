package nl.shekho.videoplayer.api.entities

data class VideoResponseEntity(
    var id: String,
    var videoContainerName: String?,
    var videoURL: String,
    var timeExpire: String,
    var sessionId: String,
    var logbookId: String
)
