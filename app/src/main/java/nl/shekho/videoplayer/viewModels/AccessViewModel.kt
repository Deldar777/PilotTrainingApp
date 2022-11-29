package nl.shekho.videoplayer.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.api.ApiService
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.SessionInformation
import nl.shekho.videoplayer.helpers.UserPreferences
import nl.shekho.videoplayer.models.LoginUser
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker,
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : ViewModel()  {

    //Response information
    var succeeded = mutableStateOf(false)
    var failed: String by mutableStateOf("")


    //Session information
    var name: MutableState<String?> = mutableStateOf("")
    var loggedIn = mutableStateOf(false)
    var jwtToken: String by mutableStateOf("")

    fun logIn(username: String, password: String){
        viewModelScope.launch {
            try {
                var loginUser = LoginUser(username,password)
                val response = apiService.login( loginUser)
                print(response)

                if(response.isSuccessful){
                    val body = response.body()

                    if(body != null){
                        succeeded.value = true
                        userPreferences.save(SessionInformation.JWTTOKEN, body.token)
                        loggedIn.value = true
                        jwtToken = body.token

                    }

                }else{
                    failed = response.message()
                }
            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }

    suspend fun save(key: String, value: String){
        userPreferences.save(key, value)
    }

    suspend fun read(key: String): String?{
        return userPreferences.read(key)
    }

}