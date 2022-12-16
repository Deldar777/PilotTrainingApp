package nl.shekho.videoplayer.views.noteCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.views.VideoView
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun FeedbackAndVideoSection(
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {

        // Video section
        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            VideoView(accessViewModel = accessViewModel)
        }

        //Feedback section
        Box(
            modifier = Modifier
                .weight(1.2f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            FeedbackSection(
                sessionViewModel = sessionViewModel,
                accessViewModel = accessViewModel
            )
        }
    }
}


