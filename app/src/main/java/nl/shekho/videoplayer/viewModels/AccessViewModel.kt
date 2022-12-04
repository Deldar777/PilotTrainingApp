package nl.shekho.videoplayer.viewModels

import androidx.compose.runtime.MutableState
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
import nl.shekho.videoplayer.api.entities.UserEntity
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.SessionInformation
import nl.shekho.videoplayer.helpers.UserPreferences
import nl.shekho.videoplayer.models.Role
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
    var loggedIn = mutableStateOf(false)
    var encodedJwtToken: String? by mutableStateOf("")
    var decodedJwtToken: JWT? = null

    //Session information
    var userIsInstructor: MutableState<Boolean> = mutableStateOf(false)
    var name = "Deldar"
    var userId: String? = ""
    var userRole: String? = ""
    var companyId: String? = ""
    var jwtExpired: Boolean? = false
    var listUsers: List<UserEntity>? = listOf()

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

    private fun getUsersByCompanyId(){
        viewModelScope.launch{
            try {
                if(companyId != null && encodedJwtToken != null){
                    val response = apiService.getUsersByCompanyId(companyId = companyId!!,token = "Bearer $encodedJwtToken")

                    if (response.isSuccessful) {
                        val body = response.body()

                        if (body != null) {
                            var users = response.body()
                            if (users != null) {
                                listUsers = users.results
                            }
                        }

                    } else {
                        failed = "Something went wrong! Try to log out and log in again"
                    }
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
            userRole = decodedJwtToken!!.getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role").asString()

            if(userRole == Role.INSTRUCTOR.type){
                userIsInstructor.value = true
            }

            jwtExpired = decodedJwtToken!!.isExpired(10)

            if(jwtExpired == true){
                loggedIn.value = false
            }
            getUsersForInstructor()
        }
    }

    private fun getUsersForInstructor(){
        if(companyId != "" && userIsInstructor.value){
            getUsersByCompanyId()
        }
    }
}