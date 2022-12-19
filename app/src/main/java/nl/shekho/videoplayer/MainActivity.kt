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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import nl.shekho.videoplayer.ui.theme.VideoPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import nl.shekho.videoplayer.ui.theme.pilotTrainingThemes.PilotTrainingTheme
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.ReviewView
import nl.shekho.videoplayer.views.SessionView
import nl.shekho.videoplayer.views.navigation.SetupNavGraph
import kotlin.time.ExperimentalTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @OptIn(ExperimentalTime::class)
    private val sessionViewModel by viewModels<SessionViewModel>()
    private val accessViewModel by viewModels<AccessViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalTime::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PilotTrainingTheme {
                navController = rememberNavController()

                //Check if the user has token stored
                ReadSessionInformation()

                //Based on the token determine which screen should be shown next
                accessViewModel.loggedIn.value =
                    accessViewModel.encodedJwtToken?.isNotEmpty() == true && accessViewModel.jwtExpired != (true
                        ?: false)

                SessionView(
                    sessionViewModel = sessionViewModel,
                    navController = navController,
                    accessViewModel = accessViewModel,
                    context = this
                )
                ReviewView(
                    sessionViewModel = sessionViewModel,
                    navController = navController,
                    accessViewModel = accessViewModel,
                    context = this
                )



                SetupNavGraph(
                    navController = navController,
                    accessViewModel = accessViewModel,
                    sessionViewModel = sessionViewModel,
                    context = this
                )
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