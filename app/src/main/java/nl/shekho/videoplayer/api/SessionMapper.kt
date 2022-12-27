package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.SessionEntity
import nl.shekho.videoplayer.models.Session

class SessionMapper {

    fun map(entity: List<SessionEntity>): Result<List<Session>> = runCatching {
        entity.map { sessionEntity ->  mapEntityToModel(sessionEntity)}
    }

    fun mapEntityToModel(entity: SessionEntity): Session {
        return Session(
            id = entity.id,
            companyId = entity.companyId,
            startTime = entity.startTime,
            videos = entity.videos,
            status = entity.status
        )
    }
}