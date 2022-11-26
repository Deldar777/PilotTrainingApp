package nl.shekho.videoplayer.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.highlightSectionCells.HighlightSection
import nl.shekho.videoplayer.views.noteCells.FeedbackAndVideoSection
import nl.shekho.videoplayer.views.topbarCells.TopBar
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun OverviewView(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel
){
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {

        Column {
            // Top bar
            TopBar(accessViewModel = accessViewModel)


            // Side bar and new session and review
            SessionsAndReview(
                accessViewModel = accessViewModel,
                sessionViewModel = sessionViewModel
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun SessionsAndReview(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel
) {
    val shape = RoundedCornerShape(20.dp)

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
    ) {

        //sessions overview
        Box(
            modifier = Modifier
                .weight(0.75f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter,
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                //Welcome text
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    accessViewModel.name.value?.let {
                        Text(
                            text = "Welcome $it!",
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                }
            }





        }

        //New session and overview block
        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.background)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

        }

    }
}