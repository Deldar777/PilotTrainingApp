package nl.shekho.videoplayer.views.overviewCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import nl.shekho.videoplayer.ui.theme.highlightItemGray
import nl.shekho.videoplayer.ui.theme.selectedItemLightBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.NoInternetView
import nl.shekho.videoplayer.views.highlightSectionCells.HighlightItem
import kotlin.time.ExperimentalTime
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.views.generalCells.ShowFeedback
import nl.shekho.videoplayer.R

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
@Composable
fun SessionItems(
    activeHighlightColor: Color = selectedItemLightBlue,
    sessionViewModel: SessionViewModel
) {
    var selectedItemIndex by remember {
        mutableStateOf(100)
    }

    val sessions by sessionViewModel.sessions.collectAsState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        if (sessionViewModel.isOnline()) {

            //Fetch the sessions from the API
            sessionViewModel.getSessionsMockData()

            sessions?.let { sessions ->
                sessions
                    .onSuccess {
                        if (it.isNotEmpty()) {
                            LazyColumn{
                                itemsIndexed(items = it) { index, session ->

                                    SessionItem(
                                        session = session,
                                        isSelected = index == selectedItemIndex,
                                        activeHighlightColor = activeHighlightColor
                                        ) {
                                        selectedItemIndex = index
                                    }
                                }
                            }

                        } else {
                            ShowFeedback(
                                text = stringResource(id = R.string.noSessionsYet),
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