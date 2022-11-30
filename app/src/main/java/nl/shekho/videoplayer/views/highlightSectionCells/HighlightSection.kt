package nl.shekho.videoplayer.views.highlightSectionCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun HighlightSection(
    sessionViewModel: SessionViewModel
){
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        //  Highlight title
        HighlightTitle()

        // Highlights section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
                .background(MaterialTheme.colors.onBackground),
            contentAlignment = Alignment.TopCenter
        ){
            // Highlights lazy columns
            HighlightItems(
                sessionViewModel = sessionViewModel
            )
        }

        // Marked event button and current altitude
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.3f),
            contentAlignment = Alignment.TopCenter
        ){
            MarkEventAndAltitudeSection(sessionViewModel = sessionViewModel)
        }
    }
}