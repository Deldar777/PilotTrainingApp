package nl.shekho.videoplayer.models

import android.graphics.Color
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.orange

data class Event(
    var id: String,
    var logbookId: String,
    var timeStamp: Int,
    var eventType: String,
    var captain: String?,
    var firstOfficer: String?,
    var feedbackAll: String?,
    var feedbackCaptain: String?,
    var feedbackFirstOfficer: String?,
    var ratingAll: Int?,
    var ratingCaptain: Int?,
    var ratingFirstOfficer: Int?
)