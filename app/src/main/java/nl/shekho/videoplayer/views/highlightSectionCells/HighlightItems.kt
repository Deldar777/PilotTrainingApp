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
import nl.shekho.videoplayer.ui.theme.highlightItemGray
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.FeedbackMessage
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.views.generalCells.NoInternetView
import nl.shekho.videoplayer.views.generalCells.ShowFeedback


@OptIn(ExperimentalTime::class)
@Composable
fun HighlightItems(
    activeHighlightColor: Color = lightBlue,
    inactiveColor: Color = highlightItemGray,
    sessionViewModel: SessionViewModel
) {

    if (sessionViewModel.events.isEmpty()) {
        ShowFeedback(
            color = Color.White,
            text = stringResource(id = R.string.noEventsYet)
        )
    }

    LazyColumn {

        itemsIndexed(items = sessionViewModel.events) { index, event ->
            if (event != null) {
                HighlightItem(
                    event = event,
                    isSelected = index == sessionViewModel.selectedItemIndex.value,
                    activeHighlightColor = activeHighlightColor,
                    inactiveColor = inactiveColor,
                ) {
                    sessionViewModel.addNoteButtonEnabled.value = false
                    sessionViewModel.selectedItemIndex.value = index
                    sessionViewModel.selectedEvent.value = event
                    sessionViewModel.getRating()
                }
            }
        }
    }
}

