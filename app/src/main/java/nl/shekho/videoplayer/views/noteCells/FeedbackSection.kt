package nl.shekho.videoplayer.views.noteCells


import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.ui.theme.feedbackBlockBackground
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.R

@OptIn(ExperimentalTime::class, ExperimentalComposeUiApi::class)
@Composable
fun FeedbackSection(
    sessionViewModel: SessionViewModel,
    initialSelectedTab: Int = 1,
    initialSelectedTabFeedback: Int = 0,
    initialRating: Int = 0,
    activeHighlightColor: Color = lightBlue,
    inactiveColor: Color = tabBackground,
    accessViewModel: AccessViewModel
) {

    //Rating states
    var ratingState by remember { mutableStateOf(initialRating) }
    var selectedRating by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (selectedRating) 60.dp else 52.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    var selectedParticipantTabIndex by remember { mutableStateOf(initialSelectedTab) }
    var selectedFeedbackSelectionTabIndex by remember { mutableStateOf(initialSelectedTabFeedback) }

    val participantTabs = listOf(
        accessViewModel.participant1?.firstname
            ?: run { stringResource(id = R.string.participant1) },
        stringResource(id = R.string.both),
        accessViewModel.participant2?.firstname
            ?: run { stringResource(id = R.string.participant2) },
    )

    val feedbackTabs = listOf(
        stringResource(id = R.string.selectFeedback),
        stringResource(id = R.string.writeFeedback),
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onBackground)
    ) {

        //Event details section
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            EventDetailsSection(
                sessionViewModel = sessionViewModel
            )
        }

        //Add note section
        Box(
            modifier = Modifier
                .weight(2.5f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                //Participants tabs
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    for (index in participantTabs.indices) {
                        ParticipantTabs(
                            tabName = participantTabs.get(index),
                            isSelected = index == selectedParticipantTabIndex,
                            activeHighlightColor = activeHighlightColor,
                            inactiveColor = inactiveColor,
                        ) {
                            selectedParticipantTabIndex = index
                        }
                    }
                }

                //Note block
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(feedbackBlockBackground)
                ) {

                    //Feedback selection and rating
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier.
                                    fillMaxSize()
                        ) {

                            //Note selections
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f)
                            ){
                                Row(
                                    modifier = Modifier.
                                        padding(6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(0.2f)
                                            .fillMaxHeight()
                                    ){
                                        //Note title
                                        Text(
                                            text = "${stringResource(id = R.string.note)}:",
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                        )
                                    }

                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    ){
                                        //Feedback selection tabs
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            for (indexFeedback in feedbackTabs.indices) {
                                                ParticipantTabs(
                                                    tabName = feedbackTabs.get(indexFeedback),
                                                    isSelected = indexFeedback == selectedFeedbackSelectionTabIndex,
                                                    activeHighlightColor = activeHighlightColor,
                                                    inactiveColor = inactiveColor,
                                                ) {
                                                    selectedFeedbackSelectionTabIndex = indexFeedback
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            //Rating section
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ){

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ){
                                        //Rating title
                                        Text(
                                            text = "${stringResource(id = R.string.rating)}:",
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                        )

                                    }


                                    //Rating stars
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ){
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            for (i in 1..5) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.star_image_foreground),
                                                    contentDescription = "",
                                                    modifier = Modifier
                                                        .width(size)
                                                        .height(size)
                                                        .pointerInteropFilter {
                                                            when (it.action) {
                                                                MotionEvent.ACTION_DOWN -> {
                                                                    selectedRating = true
                                                                    ratingState = i
                                                                }
                                                                MotionEvent.ACTION_UP -> {
                                                                    selectedRating = false
                                                                }
                                                            }
                                                            true
                                                        },
                                                    tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
                                                )
                                            }
                                        }


                                    }
                                }



                            }
                        }

                    }

                    //Feedback block and submit button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {

                    }

                }
            }
        }
    }
}
