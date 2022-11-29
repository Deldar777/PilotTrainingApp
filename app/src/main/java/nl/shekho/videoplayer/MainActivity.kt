package nl.shekho.videoplayer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import nl.shekho.videoplayer.ui.theme.VideoPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import nl.shekho.videoplayer.ui.theme.pilotTrainingThemes.PilotTrainingTheme
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.LoginView
import nl.shekho.videoplayer.views.OverviewView
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalTime::class)

    private val sessionViewModel by viewModels<SessionViewModel>()
    private val accessViewModel by viewModels<AccessViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PilotTrainingTheme {
                ReadSessionInformation()

                accessViewModel.loggedIn = accessViewModel.encodedJwtToken?.isNotEmpty() ?: false

                if(accessViewModel.loggedIn){
                    accessViewModel.decodeJWT()
                    OverviewView(accessViewModel,sessionViewModel)
                }else{
                    LoginView(accessViewModel)
                }
            }
        }
    }

    private fun ReadSessionInformation(
    ) {
        lifecycleScope.launchWhenResumed {
             accessViewModel.readJWT()
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VideoPlayerTheme {
        Greeting("Android")
    }
}