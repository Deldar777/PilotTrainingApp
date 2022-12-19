package nl.shekho.videoplayer.models

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val role: String,
    val company: String
)
