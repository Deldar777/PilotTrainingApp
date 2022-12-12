package nl.shekho.videoplayer.models

enum class LiveStreamingSetup(val type: String) {
    CREATEASSET("Creating asset..."),
    PUBLISHASSET("Publishing asset..."),
    CREATELIVEEVENT("Creating live event..."),
    SETUPSTREAMINGPLATFORM("Setup streaming platform..."),
    STARTLIVEEVENT("Starting live event..."),
    FETCHLIVESTREAMINGURL("Fetching live streaming url..."),
    STARTSTREAMINGPLATFORM("Starting streaming platform..."),
}