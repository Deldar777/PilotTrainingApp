package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.selectedItemLightBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun NewSessionButton(sessionViewModel: SessionViewModel){
    Box(
        modifier = Modifier
            .width(260.dp)
            .height(60.dp)
            .background(selectedItemLightBlue, shape = RoundedCornerShape(20.dp))
            .clickable {
                sessionViewModel.showNewSessionWindow.value = true
                sessionViewModel.showReviewWindow.value = false
                sessionViewModel.selectedSessionIndex.value = 1000
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "",
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterStart),
            tint = MaterialTheme.colors.primary
        )

        Text(
            text = stringResource(id = R.string.newSession),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}