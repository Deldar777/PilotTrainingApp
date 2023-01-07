package nl.shekho.videoplayer.views.generalCells

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.viewModels.VideoPlayerViewModel
import nl.shekho.videoplayer.views.highlightSectionCells.HighlightSection
import nl.shekho.videoplayer.views.noteCells.ReviewFeedbackAndVideoSection
import nl.shekho.videoplayer.views.noteCells.SessionFeedbackAndLiveStreaming
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun HighlightAndVideo(
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel,
    screen: String,
    navController: NavController,
    context: Context
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
                sessionViewModel = sessionViewModel,
                screen = screen,
                navController = navController,
                accessViewModel = accessViewModel
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
            if(screen == stringResource(id = R.string.session)){
                //Feedback and video section
                SessionFeedbackAndLiveStreaming(
                    sessionViewModel = sessionViewModel,
                    accessViewModel = accessViewModel,
                    context = context
                )
            }else{
                ReviewFeedbackAndVideoSection(
                    sessionViewModel = sessionViewModel,
                    accessViewModel = accessViewModel,
                    context = context
                )
            }

        }
    }
}