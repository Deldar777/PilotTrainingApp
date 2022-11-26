package nl.shekho.videoplayer.helpers

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.first
import nl.shekho.videoplayer.helpers.contracts.UserPreferencesInterface
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val userPreferences: DataStore<Preferences>
):UserPreferencesInterface  {

    override suspend fun save(key: String, value: String){
        val datastoreKey = preferencesKey<String>(key)

        userPreferences.edit {preferences ->
            preferences[datastoreKey] = value
        }
    }

    override suspend fun read(key: String): String?{
        val datastoreKey = preferencesKey<String>(key)
        val preferences = userPreferences.data.first()
        return preferences[datastoreKey]
    }
}