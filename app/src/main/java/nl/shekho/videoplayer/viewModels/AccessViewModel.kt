package nl.shekho.videoplayer.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import nl.shekho.videoplayer.api.ApiService
import nl.shekho.videoplayer.helpers.ConnectivityChecker
import javax.inject.Inject

@HiltViewModel
class AccessViewModel @Inject constructor(
    private val connectivityChecker: ConnectivityChecker,
    private val apiService: ApiService
) : ViewModel()  {

    var succeeded: Boolean by mutableStateOf(false)
    var failed: String by mutableStateOf("")
    var loggedIn: Boolean by mutableStateOf(false)


//    fun login(username: String, password: String){
//        viewModelScope.launch {
//            try {
//                val response = apiService.login(username, password)
//                succeeded = response.isSuccessful
//                failed = response.message()
//            } catch (e: java.lang.Exception) {
//                failed = e.message.toString()
//            }
//        }
//
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(): Boolean {
        return connectivityChecker.isOnline()
    }
}