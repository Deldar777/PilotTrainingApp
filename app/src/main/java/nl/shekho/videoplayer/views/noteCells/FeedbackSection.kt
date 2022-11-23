package nl.shekho.videoplayer.views.noteCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import nl.shekho.videoplayer.ui.theme.feedbackBlockBackground
import nl.shekho.videoplayer.ui.theme.selectedItemLightBlue
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FeedbackSection(
    sessionViewModel: SessionViewModel,
    initialSelectedParticipantTab: Int = 1,
    activeHighlightColor: Color = selectedItemLightBlue,
    inactiveColor: Color = tabBackground,
    ) {

    var selectedTabIndex by remember {
        mutableStateOf(initialSelectedParticipantTab)
    }
    val participantTabs = listOf<String>("First officer", "Both", "Captain")

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onBackground)
    ) {

        //Event details section
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight(),
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
                Row(
                    modifier = Modifier.fillMaxWidth()
                ){

                    for(index in participantTabs.indices){
                        ParticipantTabs(
                            tabName = participantTabs.get(index),
                            isSelected = index == selectedTabIndex,
                            activeHighlightColor = activeHighlightColor,
                            inactiveColor = inactiveColor,
                        ){
                            selectedTabIndex = index
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(feedbackBlockBackground)
                ){

                }

            }



        }
    }
}
