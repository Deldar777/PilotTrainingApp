package nl.shekho.videoplayer.api.entities

import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.models.Record

data class LogBookEntity(
    var id: String,
    var videoId: String,
    var events: List<Event?>,
    var records: List<Record?>
)
