package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.views.generalCells.ShowFeedback
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.models.Session
import nl.shekho.videoplayer.views.generalCells.NoInternetView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalTime::class)
@Composable
fun SessionItems(
    activeHighlightColor: Color = lightBlue,
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel
) {

    //Get sessions that the logged in user has participated in
    val sessions by sessionViewModel.sessions.collectAsState()
    val noSessionsInstructor = stringResource(id = R.string.noSessionsYetInstructor)
    val noSessionsPilot = stringResource(id = R.string.noSessionsYetPilot)

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        if (sessionViewModel.isOnline()) {
            sessionViewModel.fetchSessionsByUserId(
                userId = accessViewModel.loggedInUserId!!,
                token = accessViewModel.encodedJwtToken!!
            )

            sessions?.let { sessions ->
                sessions
                    .onSuccess {
                        if (it.isNotEmpty()) {
                            LazyColumn {
                                itemsIndexed(items = it) { index, session ->
                                    SessionItem(
                                        session = session,
                                        isSelected = index == sessionViewModel.selectedSessionIndex.value,
                                        activeHighlightColor = activeHighlightColor
                                    ) {
                                        //Control different windows
                                        sessionViewModel.selectedSessionIndex.value = index
                                        sessionViewModel.selectedSession.value = session
                                        sessionViewModel.showNewSessionWindow.value = false
                                        sessionViewModel.showEmptyReview.value = false
                                        sessionViewModel.showReviewWindow.value = true

                                    }
                                }
                            }

                        } else {
                            ShowFeedback(
                                text = if (accessViewModel.userIsInstructor.value) noSessionsInstructor else noSessionsPilot,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                    .onFailure {
                        ShowFeedback(
                            text = stringResource(id = R.string.generalError),
                            color = Color.Red
                        )
                    }

            } ?: run {
                CircularProgressIndicator(color = Color.White)
            }

        } else {
            NoInternetView()
        }
    }
}

fun filterSessionIfUserIsInstructor(sessions: List<Session>) {

    sessions.forEach { session ->
        val sessionDateString = session.startTime
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        val sessionDate = LocalDate.parse(sessionDateString, formatter);
    }

}