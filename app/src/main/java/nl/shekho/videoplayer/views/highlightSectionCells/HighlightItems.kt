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
    val events by sessionViewModel.events.collectAsState()

    if (sessionViewModel.isOnline()) {
        //Fetch the events from the API
        sessionViewModel.getEventsMockData()

//        sessionViewModel.selectedEvent.value = eventList.get(eventList.lastIndex)
//        val coroutineScope = rememberCoroutineScope()
//        val scrollState = rememberLazyListState()
        events?.let { listEvent ->
            listEvent
                .onSuccess {
                    if (it.isNotEmpty()) {
                        LazyColumn {
                            itemsIndexed(items = it) { index, event ->
                                HighlightItem(
                                    event = event,
                                    isSelected = index == sessionViewModel.selectedItemIndex.value,
                                    activeHighlightColor = activeHighlightColor,
                                    inactiveColor = inactiveColor,
                                ){
                                    sessionViewModel.selectedItemIndex.value = index
                                    sessionViewModel.selectedEvent.value = event
                                }
                            }
                        }

                    } else {
                        ShowFeedback(
                            text = stringResource(id = R.string.noEventsYet),
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

