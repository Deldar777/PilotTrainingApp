package nl.shekho.videoplayer.api.entities

import java.time.LocalDateTime

data class SessionEntity(
    var id: String,
    var startTime: String,
    var companyId: String,
    var videos: List<String>?
)