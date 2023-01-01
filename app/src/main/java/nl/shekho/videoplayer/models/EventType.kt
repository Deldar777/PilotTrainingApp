package nl.shekho.videoplayer.models

enum class EventType(val type: String) {
    TAKE_OFF("Take off"),
    MASTER_WARNING("Master warning"),
    MASTER_CAUTION("Master caution"),
    ENGINE_FAILURE("Engine failure"),
    ENGINE_FIRE("Engine fire"),
    MARKED_EVENT("Marked event"),
    TCAS("TCAS"),
    LANDING("Landing"),
}