package nl.shekho.videoplayer.models

import java.time.LocalDateTime

data class Session(
    var id: String?,
    var startTime: String,
    var companyId: String?,
    var videos: List<String>?
)
