package nl.shekho.videoplayer.models

data class User(
    val username: String,
    val password: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val type: RoleType,
    val company: Company
)
