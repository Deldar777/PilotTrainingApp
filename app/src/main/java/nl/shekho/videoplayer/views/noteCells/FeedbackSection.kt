package nl.shekho.videoplayer.views.noteCells


import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.models.EventType
import nl.shekho.videoplayer.ui.theme.*
import java.time.LocalDateTime

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

    //Add note button states
    var addNoteButtonEnabled by remember {
        mutableStateOf(false)
    }

    //Rating states
    var ratingState by remember { mutableStateOf(initialRating) }
    var selectedRating by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (selectedRating) 60.dp else 52.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    //Feedback blocks states
    var showCommonFeedback by remember { mutableStateOf(true) }

    //Written feedback text field
    var writtenFeedback by remember { mutableStateOf("") }

    // Common feedback dropdown 1
    val commonFeedbackList = listOf<String>(
        stringResource(id = R.string.highSpeed),
        stringResource(id = R.string.AltitudeHigh),
        stringResource(id = R.string.altitudeLow),
    )
    val noteBoth = stringResource(id = R.string.noteBoth)
    val noteParticipant = stringResource(id = R.string.noteParticipants)
    var feedbackInstruction by remember {
        mutableStateOf(noteBoth)
    }
    var expanded1 by remember { mutableStateOf(false) }
    var selectedCommonFeedback by remember { mutableStateOf("") }
    var textfieldSize1 by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val icon1 = if (expanded1)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

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
                            if (index == 0 || index == 2) {
                                feedbackInstruction =
                                    "$noteParticipant ${participantTabs.get(index)}..."
                            } else {
                                feedbackInstruction = noteBoth
                            }
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
                            modifier = Modifier.fillMaxSize()
                        ) {

                            //Note selections
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(0.2f)
                                            .fillMaxHeight()
                                    ) {
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
                                    ) {
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
                                                    selectedFeedbackSelectionTabIndex =
                                                        indexFeedback
                                                    showCommonFeedback = indexFeedback == 0
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
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
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
                                    ) {
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
                                                            addNoteButtonEnabled = true
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
                                                    tint = if (i <= ratingState) Color(0xFFFFD700) else Color(
                                                        0xFFA2ADB1
                                                    )
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

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp)
                        ) {
                            //Dropdown and text field section
                            Box(
                                contentAlignment = Alignment.TopCenter,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                if (showCommonFeedback) {
                                    Column {
                                        OutlinedTextField(
                                            colors = TextFieldDefaults.textFieldColors(
                                                backgroundColor = Color.White,
                                                textColor = MaterialTheme.colors.primaryVariant
                                            ),
                                            value = selectedCommonFeedback,
                                            onValueChange = {
                                                selectedCommonFeedback = it
                                                addNoteButtonEnabled = true
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .onGloballyPositioned { coordinates ->
                                                    //This value is used to assign to the DropDown the same width
                                                    textfieldSize1 = coordinates.size.toSize()
                                                },
                                            label = {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                ) {

                                                    Text(
                                                        text = stringResource(id = R.string.commonFeedback),
                                                        color = textSecondaryDarkMode
                                                    )
                                                }
                                            },
                                            trailingIcon = {
                                                Icon(
                                                    icon1, null,
                                                    Modifier.clickable { expanded1 = !expanded1 },
                                                    tint = textSecondaryDarkMode
                                                )
                                            }
                                        )
                                        DropdownMenu(
                                            expanded = expanded1,
                                            onDismissRequest = { expanded1 = false },
                                            modifier = Modifier
                                                .width(with(LocalDensity.current) { textfieldSize1.width.toDp() })
                                        ) {
                                            commonFeedbackList.forEach { label ->
                                                DropdownMenuItem(onClick = {
                                                    selectedCommonFeedback = label
                                                }) {
                                                    Text(text = label)
                                                }
                                            }
                                        }
                                    }

                                } else {

                                    // written feedback textField
                                    OutlinedTextField(
                                        colors = TextFieldDefaults.textFieldColors(
                                            backgroundColor = Color.White,
                                            textColor = MaterialTheme.colors.primaryVariant
                                        ),
                                        value = writtenFeedback,
                                        onValueChange = {
                                            writtenFeedback = it
                                            if (it != "") {
                                                addNoteButtonEnabled = true
                                            }
                                        },
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colors.primaryVariant,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        label = {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = feedbackInstruction,
                                                    color = textSecondaryDarkMode
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )

                                }
                            }

                            //Add note button section
                            Box(
                                contentAlignment = Alignment.BottomEnd,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    OutlinedButton(
                                        enabled = addNoteButtonEnabled,
                                        onClick = {
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = deepPurple,
                                            contentColor = MaterialTheme.colors.primary,
                                        ),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .width(160.dp)
                                            .height(40.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.feedback_logo),
                                            contentDescription = "",
                                            tint = MaterialTheme.colors.primary,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(end = 4.dp)
                                        )
                                        Text(
                                            text = stringResource(id = R.string.addNote),
                                            fontFamily = FontFamily.Monospace,
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colors.primary,
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
