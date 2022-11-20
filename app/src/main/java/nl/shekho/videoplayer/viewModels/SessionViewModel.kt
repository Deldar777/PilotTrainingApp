package nl.shekho.videoplayer.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.models.EventType
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SessionViewModel @Inject constructor()
    : ViewModel() {

    val events: MutableState<List<Event>> = mutableStateOf(ArrayList())

    init {
        events.value = Event.getEventListMockData()
    }


}