package nl.shekho.videoplayer.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.*
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.HighlightAndVideo
import nl.shekho.videoplayer.views.highlightSectionCells.*
import nl.shekho.videoplayer.views.navigation.Screens
import nl.shekho.videoplayer.views.topbarCells.TopBarSession
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SessionView(
    sessionViewModel: SessionViewModel,
    navController: NavController,
    accessViewModel: AccessViewModel,
    context: Context
) {

    val cannotSaveSession = stringResource(id = R.string.cannotSaveSession)

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()

    ) {
        Column {

            // Top bar
            TopBarSession(
                sessionViewModel = sessionViewModel,
                accessViewModel = accessViewModel
            )

            // Middle part of the screen (highlight - video and add feedback block)
            HighlightAndVideo(
                sessionViewModel = sessionViewModel,
                accessViewModel = accessViewModel,
                screen = stringResource(id = R.string.session),
                navController = navController,
                context = context
            )
        }

        //To put a white overlay on the screen if end session was asked
        if (sessionViewModel.openDialog.value) {
            Box(
                modifier = Modifier
                    .background(color = Color.White.copy(alpha = 0.8f))
                    .fillMaxSize()
            ) {
            }
        }

        //Save session process
        if (sessionViewModel.saveSessionAsked) {
            if (sessionViewModel.savingSession) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 220.dp)
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else {
                if (!sessionViewModel.savingSessionSucceeded) {
                    Toast.makeText(context, cannotSaveSession, Toast.LENGTH_LONG).show()
                } else {
                    sessionViewModel.openDialog.value = false
                    navController.navigate(Screens.OverviewScreen.route)
                }
                sessionViewModel.saveSessionAsked = false
            }
        }

    }
}




