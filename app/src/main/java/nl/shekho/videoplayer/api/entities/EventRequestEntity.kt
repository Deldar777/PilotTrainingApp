package nl.shekho.videoplayer.api.entities

data class EventRequestEntity(
    var timeStamp: Int,
    var eventType: String,
    var logbookId: String,
    var captain: String?,
    var firstOfficer: String?,
    var feedbackAll: String?,
    var feedbackCaptain: String?,
    var feedbackFirstOfficer: String?,
    var ratingAll: Int?,
    var ratingCaptain: Int?,
    var ratingFirstOfficer: Int?,
)
