package nl.shekho.videoplayer.api.entities

data class StreamingEntity(
    var StreamName: String,
    var Description: String,
    var StreamStatus: String,
    var IngestURL: String,
    var PreviewURL: String,
)