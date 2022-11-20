package nl.shekho.videoplayer.models

enum class EventType(val type: String) {
    TAKEOFF("Take off"),
    MASTERWARNING("Master warning"),
    MASTERCAUTION("Master caution"),
    ENGINEFAILURE("Engine failure"),
    ENGINEFIRE("Engine fire"),
    MARKEDEVENT("Marked event"),
    TCAS("TCAS"),
    LANDING("Landing"),
}