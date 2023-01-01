package nl.shekho.videoplayer.views.highlightSectionCells

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.helpers.extensions.Helpers
import nl.shekho.videoplayer.ui.theme.highlightItemGray
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.FeedbackMessage
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.views.generalCells.NoInternetView
import nl.shekho.videoplayer.views.generalCells.ShowFeedback
import nl.shekho.videoplayer.views.overviewCells.SessionItem


@OptIn(ExperimentalTime::class)
@Composable
fun HighlightItems(
    activeHighlightColor: Color = lightBlue,
    inactiveColor: Color = highlightItemGray,
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel
) {

    //Get sessions that the logged in user has participated in
    val logBook by sessionViewModel.logBook.collectAsState()

    if (sessionViewModel.isOnline()) {

        //Get the logBook for the session (Event and records)
        sessionViewModel.getLogBookById(
            logBookId = "027e23c0-39e5-4a0c-94fe-08daebee5de3",
            token = accessViewModel.encodedJwtToken!!
        )

        logBook?.let { listEvents ->
            listEvents
                .onSuccess {

                    if (it.events.isNotEmpty()) {

                        LazyColumn {
                            itemsIndexed(items = it.events) { index, event ->
                                if (event != null) {
                                    HighlightItem(
                                        sessionViewModel = sessionViewModel,
                                        event = event,
                                        isSelected = index == sessionViewModel.selectedItemIndex.value,
                                        activeHighlightColor = activeHighlightColor,
                                        inactiveColor = inactiveColor,
                                    ) {
                                        sessionViewModel.addNoteButtonEnabled.value = false
                                        sessionViewModel.selectedItemIndex.value = index
                                        sessionViewModel.selectedEvent.value = event
                                        sessionViewModel.getRating()
                                        sessionViewModel.getFeedback()
                                    }
                                }
                            }
                        }
                    } else {
                        ShowFeedback(
                            color = Color.White,
                            text = stringResource(id = R.string.noEventsYet)
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

