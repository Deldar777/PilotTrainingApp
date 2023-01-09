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


//    sessionViewModel.startLiveEvent(
//        videoId = "56874348-0ff1-4773-a26c-852e1cd25fd0",
//        token = "eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNobWFjLXNoYTI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJST0xFX0lOU1RSVUNUT1IiLCJDb21wYW55SWQiOiJkNjg0ZGFlMy03NDJiLTQ4ZDQtYjVjMC0wYmJkMDZiYzVjM2EiLCJVc2VySWQiOiI0ZmI4ZGRjNy02NzFhLTQ0ZWMtODBhYy0zMzE2NGE1NjYxODIiLCJuYmYiOjE2NzMzMDI1MDYsImV4cCI6MTY3MzM4ODkwNiwiaWF0IjoxNjczMzAyNTA2LCJpc3MiOiJodHRwczovL3ZyZWZzb2x1dGlvbnNkZXYwMDEuYXp1cmV3ZWJzaXRlcy5uZXQvYXBpLyIsImF1ZCI6Imh0dHBzOi8vdnJlZnNvbHV0aW9uc2RldjAwMS5henVyZXdlYnNpdGVzLm5ldC9hcGkvIn0.wvN5J5Hi5azhikQENfMr_zyfgt43YbxphcESlylyOWk"
//    )
//    sessionViewModel.fetchVideoFromUrl("https://vrefsolutionsdownload.blob.core.windows.net/trainevids/OVERVIEW.mp4")
//    sessionViewModel.startLiveStreaming("https://msvrefsolutions002-euwe.streaming.media.azure.net/e1d3b9fa-dc49-4b54-a88c-df01508aafa7/255f7307-ba76-482b-823e-fa071540bc1b.ism/manifest(format=m3u8-cmaf)")


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




