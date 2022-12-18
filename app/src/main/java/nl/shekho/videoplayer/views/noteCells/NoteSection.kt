package nl.shekho.videoplayer.views.noteCells

import android.content.Context
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.ui.theme.textSecondaryDarkMode
import nl.shekho.videoplayer.viewModels.AccessViewModel

@OptIn(ExperimentalTime::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteSection(
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel,
    initialSelectedTab: Int = 1,
    activeHighlightColor: Color = lightBlue,
    inactiveColor: Color = tabBackground,
    context: Context
) {

    //Participants tabs
    var selectedParticipantTabIndex by remember { mutableStateOf(initialSelectedTab) }
    var givenFeedback by remember { mutableStateOf(sessionViewModel.selectedEvent.value.feedback) }
    var editMode by remember { mutableStateOf(false) }
    val participantTabs = listOf(
        accessViewModel.participant1?.firstname
            ?: run { stringResource(id = R.string.participant1) },
        stringResource(id = R.string.both),
        accessViewModel.participant2?.firstname
            ?: run { stringResource(id = R.string.participant2) },
    )

    //Rating states
    var ratingState by remember { mutableStateOf(4) }
    var selectedRating by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (selectedRating) 60.dp else 52.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    //Edit mode
    val editModeOff = stringResource(id = R.string.editOff)
    val editModeOn = stringResource(id = R.string.editOn)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onBackground)
    ) {

        //Note details section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            EventDetailsSection(
                sessionViewModel = sessionViewModel,
                title = stringResource(id = R.string.notes),
                subTitle = stringResource(id = R.string.noteSubTitle)
            )
        }

        //Feedback details section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterEnd
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                //Participants tabs
                Box(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
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
                }

                //Feedback text field
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .weight(1.8f)
                        .fillMaxHeight(),
                ) {
                    // written feedback textField
                    OutlinedTextField(
                        enabled = editMode,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = MaterialTheme.colors.primaryVariant
                        ),
                        value = givenFeedback?.let { givenFeedback } ?: "",
                        onValueChange = {
                            givenFeedback = it

                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.primaryVariant,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f)
                    ) {
                        if (accessViewModel.userIsInstructor.value) {
                            if (editMode) {
                                IconButton(onClick = {
                                    editMode = false
                                    Toast.makeText(context, editModeOff, Toast.LENGTH_LONG).show()

                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_mode_edit_24),
                                        contentDescription = "",
                                        tint = lightBlue,
                                        modifier = Modifier
                                            .size(36.dp)
                                    )
                                }
                            } else {
                                IconButton(onClick = {
                                    editMode = true
                                    Toast.makeText(context, editModeOn, Toast.LENGTH_LONG).show()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_baseline_edit_off_24),
                                        contentDescription = "",
                                        tint = lightBlue,
                                        modifier = Modifier
                                            .size(36.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }

        //Rating details section
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
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
                                            if (editMode) {
                                                selectedRating = true
                                                ratingState = i
                                            }

                                        }
                                        MotionEvent.ACTION_UP -> {
                                            if (editMode) {
                                                selectedRating = false
                                            }
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
                
                Spacer(modifier = Modifier.weight(3f))
                //Save changes button 
                
                if(accessViewModel.userIsInstructor.value){
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedButton(
                            enabled = editMode,
                            onClick = {
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = lightBlue,
                                contentColor = MaterialTheme.colors.primary
                            ),
                        ) {
                            Text(
                                text = stringResource(id = R.string.saveChanges),
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .padding(2.dp)
                            )
                        }

                    }
                }
                
            }

        }
    }

}