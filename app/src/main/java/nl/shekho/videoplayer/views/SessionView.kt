package nl.shekho.videoplayer.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.*
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.HighlightAndVideo
import nl.shekho.videoplayer.views.highlightSectionCells.*
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

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()

    ) {
        Column {

            // Top bar
            TopBarSession(
                sessionViewModel = sessionViewModel,
                accessViewModel = accessViewModel,
                navController = navController
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

        if(sessionViewModel.openDialog.value){
            Box(
                modifier = Modifier
                    .background(color = Color.White.copy(alpha = 0.8f))
                    .fillMaxSize()
            ){

            }
        }
    }
}





