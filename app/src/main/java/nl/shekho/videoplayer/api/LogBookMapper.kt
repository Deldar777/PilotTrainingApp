package nl.shekho.videoplayer.api

import nl.shekho.videoplayer.api.entities.LogBookEntity
import nl.shekho.videoplayer.models.LogBook

class LogBookMapper {

    fun map(entity: LogBookEntity): Result<LogBook> = runCatching {
        with(entity) {
            LogBook(
                id = id,
                events = events
            )
        }
    }
}