package nl.shekho.videoplayer.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.api.ApiService
import nl.shekho.videoplayer.api.UserMapper
import nl.shekho.videoplayer.api.entities.LoginEntity
import nl.shekho.videoplayer.api.entities.UserEntity
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import nl.shekho.videoplayer.helpers.SessionInformation
import nl.shekho.videoplayer.helpers.UserPreferences
import nl.shekho.videoplayer.models.Role
import nl.shekho.videoplayer.models.User
import javax.inject.Inject


@HiltViewModel
class AccessViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker,
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val userMapper: UserMapper
) : ViewModel() {

    //Response information
    var succeeded: Boolean by mutableStateOf(false)
    var failed: String by mutableStateOf("")
    var loading: Boolean by mutableStateOf(false)
    var loginAsked: Boolean by mutableStateOf(false)

    //Access information
    var loggedIn = mutableStateOf(false)
    var encodedJwtToken: String? by mutableStateOf("")
    var decodedJwtToken: JWT? = null

    //Session information
    var sessionId: String? = ""
    var userIsInstructor = mutableStateOf(false)
    var loggedInUserId: String? = ""
    var loggedInUser: User? by mutableStateOf(null)
    var firstOfficer: User? by mutableStateOf(null)
    var captain: User? by mutableStateOf(null)
    var userRole: String? = ""
    var companyId: String? = ""
    var jwtExpired: Boolean? = false
    var listUsers: List<User?> = listOf()

    fun resetSessionInformation() {
        userIsInstructor.value = false
        loggedIn.value = false
        encodedJwtToken = null
        decodedJwtToken = null
        loggedInUser = null
        userRole = null
        companyId = null
        jwtExpired = false
        listUsers = emptyList()
        save(SessionInformation.JWTTOKEN, "")
    }

    fun logIn(username: String, password: String) {
        viewModelScope.launch {
            loading = true
            try {
                val userEntity = LoginEntity(username, password)
                val response = apiService.login(userEntity)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        loggedIn.value = true
                        succeeded = true
                        userPreferences.save(SessionInformation.JWTTOKEN, body.token)
                        encodedJwtToken = body.token
                        decodeJWTAfterLogin(body.token)
                    }
                } else {
                    failed = response.message()
                }
            } catch (e: java.lang.Exception) {
                failed = e.message.toString()
            }

            loading = false
        }
    }

    private fun getUserById() {
        viewModelScope.launch {
            try {
                if (companyId != null && encodedJwtToken != null) {
                    val response = apiService.getUserById(
                        userId = loggedInUserId!!,
                        token = "Bearer $encodedJwtToken"
                    )

                    if (response.isSuccessful) {
                        val body = response.body()

                        if (body != null) {
                            val user = response.body()
                            if (user != null) {
                                val mappedUser = userMapper.mapEntityToModel(user.results)
                                loggedInUser = mappedUser
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

    private fun getUsersByCompanyId() {
        viewModelScope.launch {
            try {
                if (companyId != null && encodedJwtToken != null) {
                    val response = apiService.getUsersByCompanyId(
                        companyId = companyId!!,
                        token = "Bearer $encodedJwtToken"
                    )

                    if (response.isSuccessful) {
                        val body = response.body()

                        if (body != null) {
                            val users = response.body()
                            if (users != null) {
                                val mappedUsers = userMapper.mapListEntityToModel(users.results)
                                listUsers = mappedUsers
                                loggedInUser = listUsers.firstOrNull { it!!.id == loggedInUserId }
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
        decodeJWT()
    }

    private fun decodeJWTAfterLogin(token: String) {
        if (token.isNotEmpty()) {
            decodedJwtToken = JWT(token)
            assignValuesAfterDecoding(decodedJwtToken!!)
        }
    }

    private fun decodeJWT() {
        val token = encodedJwtToken

        //Decode the token if it is not empty
        if (!token.isNullOrEmpty()) {

            decodedJwtToken = JWT(token)

            //Stop decoding if token si expired
            jwtExpired = decodedJwtToken!!.isExpired(10)

            if (jwtExpired != true) {
                assignValuesAfterDecoding(decodedJwtToken!!)
            }
        }
    }

    private fun assignValuesAfterDecoding(decodedJwtToken: JWT) {
        loggedInUserId = decodedJwtToken.getClaim("UserId").asString()
        companyId = decodedJwtToken.getClaim("CompanyId").asString()
        userRole =
            decodedJwtToken.getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role")
                .asString()
        if (userRole == Role.INSTRUCTOR.type) {
            userIsInstructor.value = true
            getUsers()
        } else {
            userIsInstructor.value = false
            getUser()
        }
    }

    private fun getUser() {
        if (loggedInUserId != "") {
            getUserById()
        }
    }

    private fun getUsers() {
        if (companyId != "") {
            getUsersByCompanyId()
        }
    }
}