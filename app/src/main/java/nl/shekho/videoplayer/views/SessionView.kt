package nl.shekho.videoplayer.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.*
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.highlightSectionCells.*
import nl.shekho.videoplayer.views.noteCells.FeedbackAndVideoSection
import nl.shekho.videoplayer.views.topbarCells.TopBar
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SessionView(sessionViewModel: SessionViewModel) {
    val events = sessionViewModel.events.value

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Column {

            // Top bar
            TopBar(sessionViewModel = sessionViewModel)

            // Middle part of the screen (highlight - video and add feedback block)
            HighlightAndVideo(
                eventList = events,
                sessionViewModel = sessionViewModel
            )
        }
    }
}


@OptIn(ExperimentalTime::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HighlightAndVideo(
    eventList: List<Event>,
    sessionViewModel: SessionViewModel
) {
    val shape = RoundedCornerShape(20.dp)

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
    ) {

        //Highlight section
        Box(
            modifier = Modifier
                .weight(0.75f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter,
        ) {
            HighlightSection(
                eventList = eventList,
                sessionViewModel = sessionViewModel
            )
        }

        //Video and feedback section
        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.background)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            //Feedback and video section
            FeedbackAndVideoSection(
                sessionViewModel = sessionViewModel
            )
        }
    }
}


