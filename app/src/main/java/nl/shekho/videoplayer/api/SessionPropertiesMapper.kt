package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.VideoResponseEntity
import nl.shekho.videoplayer.models.SessionProperties

class SessionPropertiesMapper {
    fun mapEntityToModel(entity: VideoResponseEntity): SessionProperties {
        return SessionProperties(
            videoURL = entity.videoURL,
            sessionId = entity.sessionId,
            logbookId = entity.logbookId
        )
    }
}