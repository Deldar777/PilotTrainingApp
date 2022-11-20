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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HighlightAndVideo(
    eventList: List<Event>,
    activeHighlightColor: Color = selectedItemLightBlue,
    inactiveColor: Color = highlightItemGray,
    initialSelectedItemIndex: Int = eventList.lastIndex
) {

    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }

    var currentAltitude by remember {
        mutableStateOf(29550)
    }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
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
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter,
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                //  Highlight title
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Highlights",
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }

                // Highlights
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f)
                        .background(highlightListGray),
                    contentAlignment = Alignment.TopCenter
                ){

                    LazyColumn(state = scrollState){
                        itemsIndexed(items = eventList) { index, event ->
                            HighlightItem(
                                event = event,
                                isSelected = index == selectedItemIndex,
                                activeHighlightColor = activeHighlightColor,
                                inactiveColor = inactiveColor,
                                ){
                                selectedItemIndex = index
                            }
                        }
                        coroutineScope.launch {
                            scrollState.scrollToItem(eventList.lastIndex)
                        }
                    }
                }

                // Marked event button and current altitude
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.3f),
                    contentAlignment = Alignment.TopCenter
                ){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = deepPurple,
                                contentColor = MaterialTheme.colors.primary,
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .width(200.dp)
                                .height(50.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.feedback_logo),
                                contentDescription = "Feedback icon",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = "Mark event",
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(highlightItemGray)
                        ){
                            Text(
                                text = "Current altitude: $currentAltitude",
                                color = MaterialTheme.colors.primary,
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
