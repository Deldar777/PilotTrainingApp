package nl.shekho.videoplayer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import nl.shekho.videoplayer.ui.theme.VideoPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import nl.shekho.videoplayer.ui.theme.pilotTrainingThemes.PilotTrainingTheme
import nl.shekho.videoplayer.views.LoginView
import nl.shekho.videoplayer.views.SessionView

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PilotTrainingTheme() {

                val loggedIn = true

                if (loggedIn) {
                    SessionView()
                } else {
                    LoginView(context = applicationContext)
                }
            }
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