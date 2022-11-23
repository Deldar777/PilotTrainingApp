package nl.shekho.videoplayer.views.highlightSectionCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.highlightItemGray
import nl.shekho.videoplayer.ui.theme.selectedItemLightBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HighlightItems(
    eventList: List<Event>,
    activeHighlightColor: Color = selectedItemLightBlue,
    inactiveColor: Color = highlightItemGray,
    initialSelectedItemIndex: Int = eventList.lastIndex,
    sessionViewModel: SessionViewModel
){
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedItemIndex)
    }
    sessionViewModel.selectedEvent.value = eventList.get(eventList.lastIndex)

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState){
        itemsIndexed(items = eventList) { index, event ->
            HighlightItem(
                event = event,
                isSelected = index == selectedItemIndex,
                activeHighlightColor = activeHighlightColor,
                inactiveColor = inactiveColor,
            ){
                selectedItemIndex = index
                sessionViewModel.selectedEvent.value = event
            }
        }
        coroutineScope.launch {
            scrollState.scrollToItem(eventList.lastIndex)
        }
    }
}