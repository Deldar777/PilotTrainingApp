package nl.shekho.videoplayer.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.*
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.viewModels.VideoPlayerViewModel
import nl.shekho.videoplayer.views.cells.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SessionView() {
    val sessionViewModel = hiltViewModel<SessionViewModel>()
    val events = sessionViewModel.events.value

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Column {

            // Top bar
            NavigationBar()

            // Middle part of the screen (highlight - video and add feedback block)
            HighlightAndVideo(
                eventList = events,
                sessionViewModel = sessionViewModel
            )
        }
    }
}


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


