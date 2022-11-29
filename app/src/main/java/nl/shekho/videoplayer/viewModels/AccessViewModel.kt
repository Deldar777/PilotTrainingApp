package nl.shekho.videoplayer.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.api.ApiService
import nl.shekho.videoplayer.api.entities.LoginUser
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.SessionInformation
import nl.shekho.videoplayer.helpers.UserPreferences
import javax.inject.Inject


@HiltViewModel
class AccessViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker,
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) : ViewModel() {

    //Response information
    var succeeded = mutableStateOf(false)
    var failed: String by mutableStateOf("")


    //Access information
    var loggedIn: Boolean by mutableStateOf(false)
    var encodedJwtToken: String? by mutableStateOf("")
    var decodedJwtToken: JWT? = null

    //Session information
    var name = "Deldar"
    var userId: String? = ""
    var companyId: String? = ""
    var jwtExpired: Boolean? = false

    fun logIn(username: String, password: String) {
        viewModelScope.launch {
            try {
                var userEntity = LoginUser(username, password)
                val response = apiService.login(userEntity)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        succeeded.value = true
                        userPreferences.save(SessionInformation.JWTTOKEN, body.token)
                        encodedJwtToken = body.token
                    }

                } else {
                    failed = response.message()
                }
            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }
        }
    }

    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }

    fun save(key: String, value: String) {
        viewModelScope.launch {
            userPreferences.save(key, value)
        }
    }

    fun readJWT() {
        viewModelScope.launch {
            encodedJwtToken = userPreferences.read(SessionInformation.JWTTOKEN)
        }
    }

    fun decodeJWT() {
        val token = encodedJwtToken

        if (!token.isNullOrEmpty()) {
            decodedJwtToken = JWT(token)
            userId = decodedJwtToken!!.getClaim("UserId").asString()
            companyId = decodedJwtToken!!.getClaim("CompanyId").asString()
            jwtExpired = decodedJwtToken!!.isExpired(10)
            //val issuer = jwt.issuer
        }
    }
}