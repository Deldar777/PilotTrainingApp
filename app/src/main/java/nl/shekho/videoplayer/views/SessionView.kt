package nl.shekho.videoplayer.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.highlightListGray
import nl.shekho.videoplayer.ui.theme.lightGray
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.viewModels.VideoPlayerViewModel
import nl.shekho.videoplayer.views.cells.HighlightItem
import nl.shekho.videoplayer.views.cells.NavigationBar

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
            HighlightAndVideo(eventList = events)
        }
    }
}


@Composable
fun HighlightAndVideo(
    eventList: List<Event>
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
                .background(highlightListGray, shape)
                .fillMaxHeight()
                .padding(),
            contentAlignment = Alignment.TopCenter,
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                //  Highlight title
                Box(
                    Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Highlight",
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }

                // Highlights
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(lightGray),
                    contentAlignment = Alignment.TopCenter
                ){

                    LazyColumn{
                        itemsIndexed(items = eventList) { index, event ->
                            HighlightItem(
                                event = event
                            )
                        }
                    }
                }

            }

        }

        //Video and feedback section
        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.background)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
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

                    VideoView()
                }

                //Feedback section
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colors.onBackground, shape)
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Feedback")
                }

            }
        }

    }
}
