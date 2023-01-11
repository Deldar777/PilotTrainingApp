package nl.shekho.videoplayer.models

data class LogBook(
    var id: String,
    var events: List<Event?>,
    var records: List<Record?>,
)
