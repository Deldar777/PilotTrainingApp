package nl.shekho.videoplayer.helpers.contracts

interface UserPreferencesInterface {

    suspend fun save(key: String, value: String)
    suspend fun read(key: String): String?
}