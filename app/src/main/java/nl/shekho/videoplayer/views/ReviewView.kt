package nl.shekho.videoplayer.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.HighlightAndVideo
import nl.shekho.videoplayer.views.topbarCells.TopBarReview
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ReviewView(
    sessionViewModel: SessionViewModel,
    navController: NavController,
    accessViewModel: AccessViewModel,
    context: Context
) {


    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Column {

            // Top bar
            TopBarReview(
                sessionViewModel = sessionViewModel,
                accessViewModel = accessViewModel,
                navController = navController
            )

            // Middle part of the screen (highlight - video and add feedback block)
            HighlightAndVideo(
                sessionViewModel = sessionViewModel,
                accessViewModel = accessViewModel,
                screen = stringResource(id = R.string.review),
                navController = navController,
                context = context
            )

        }
    }
}