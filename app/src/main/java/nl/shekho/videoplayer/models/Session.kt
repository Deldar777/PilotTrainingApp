package nl.shekho.videoplayer.models


data class Session(
    var id: String,
    var startTime: String,
    var companyId: String?,
    var videos: List<String>?,
    var status: String?
)
