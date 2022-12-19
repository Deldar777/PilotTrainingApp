package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.SessionEntity
import nl.shekho.videoplayer.api.entities.UserEntity
import nl.shekho.videoplayer.api.entities.UsersEntity
import nl.shekho.videoplayer.models.Session
import nl.shekho.videoplayer.models.User

class UserMapper {

    fun map(entity: UsersEntity): Result<List<User>> = runCatching {
        entity.results.map { userEntity ->  mapEntityToModel(userEntity)}
    }

    private fun mapEntityToModel(entity: UserEntity): User {
        return User(
            id = entity.id,
            firstName = entity.firstname,
            lastName = entity.lastname,
            fullName = entity.firstname + " " + entity.lastname,
            role = entity.role,
            company = entity.companyId
        )
    }
}