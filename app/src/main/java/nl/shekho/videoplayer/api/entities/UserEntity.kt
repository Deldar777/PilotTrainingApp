package nl.shekho.videoplayer.api.entities

data class UserEntity(
    var id: String,
    var email: String,
    var firstname: String,
    var lastname: String,
    var username: String,
    var password: String,
    var role: String,
    var companyId: String
)
