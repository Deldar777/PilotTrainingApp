package nl.shekho.videoplayer.views.highlightSectionCells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.AccessViewModel

@OptIn(ExperimentalTime::class)
@Composable
fun HighlightSection(
    sessionViewModel: SessionViewModel,
    screen: String,
    navController: NavController,
    accessViewModel: AccessViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        //Back navigation to sessions overview
        if(screen == stringResource(id = R.string.review)){
            BackToSessionsReview(
                navController = navController,
                sessionViewModel = sessionViewModel
            )
        }

        //  Highlight title
        HighlightTitle()

        // Highlights section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.6f)
                .background(MaterialTheme.colors.onBackground),
            contentAlignment = Alignment.TopCenter
        ) {
            // Highlights lazy columns
            HighlightItems(
                screen = screen,
                sessionViewModel = sessionViewModel,
                accessViewModel = accessViewModel
            )
        }

        // Marked event button and current altitude

        if (screen == stringResource(id = R.string.session)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.4f),
                contentAlignment = Alignment.TopCenter
            ) {
                MarkEventAndAltitudeSection(sessionViewModel = sessionViewModel)
            }
        }

    }
}