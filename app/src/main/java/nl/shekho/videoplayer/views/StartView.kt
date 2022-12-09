package nl.shekho.videoplayer.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.topbarCells.TopBarLogin
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun StartView(
    accessViewModel: AccessViewModel,
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    var showOverview by remember { mutableStateOf(accessViewModel.loggedIn) }
    var showNewSessionButton by remember { mutableStateOf(accessViewModel.userIsInstructor) }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        if (showOverview.value) {
            OverviewView(
                accessViewModel = accessViewModel,
                sessionViewModel = sessionViewModel,
                navController = navController,
                showNewSessionButton = showNewSessionButton.value
            )

        } else {

            LoginView(
                accessViewModel = accessViewModel,
                navController = navController
            )
        }
    }
}