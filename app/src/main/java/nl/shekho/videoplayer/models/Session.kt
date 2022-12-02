package nl.shekho.videoplayer.models

import java.time.LocalDateTime

data class Session(
    var id: String?,
    var startTime: LocalDateTime?,
    var users: List<User>?,
    var company: Company?,
    var videos: List<VideoItem>?
)
