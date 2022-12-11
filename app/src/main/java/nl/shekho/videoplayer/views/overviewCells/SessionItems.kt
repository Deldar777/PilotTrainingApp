package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.viewModels.AccessViewModel

@OptIn(ExperimentalTime::class)
@Composable
fun SessionItems(
    activeHighlightColor: Color = lightBlue,
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel
) {

    //Get sessions that the logged in user has participated in
    val sessions by sessionViewModel.sessions.collectAsState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

//        if (sessionViewModel.isOnline()) {
//            sessionViewModel.fetchSessionsByUserId(userId = accessViewModel.loggedInUserId!!, token = accessViewModel.encodedJwtToken!!)
//
//            sessions?.let { sessions ->
//                sessions
//                    .onSuccess {
//                        if (it.isNotEmpty()) {
//                            LazyColumn {
//                                itemsIndexed(items = it) { index, session ->
//
//                                    SessionItem(
//                                        session = session,
//                                        isSelected = index == sessionViewModel.selectedSessionIndex.value,
//                                        activeHighlightColor = activeHighlightColor
//                                    ) {
//                                        sessionViewModel.selectedSessionIndex.value = index
//                                        sessionViewModel.selectedSession.value = session
//                                        sessionViewModel.showNewSessionWindow.value = false
//                                        sessionViewModel.showEmptyReview.value = false
//                                        sessionViewModel.showReviewWindow.value = true
//
//                                    }
//                                }
//                            }
//
//                        } else {
//                            ShowFeedback(
//                                text = stringResource(id = R.string.noSessionsYet),
//                                color = MaterialTheme.colors.primary
//                            )
//                        }
//                    }
//                    .onFailure {
//                        ShowFeedback(
//                            text = stringResource(id = R.string.generalError),
//                            color = Color.Red
//                        )
//                    }
//
//            } ?: run {
//                CircularProgressIndicator(color = Color.White)
//            }
//
//        } else {
//            NoInternetView()
//        }
    }

}