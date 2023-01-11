package nl.shekho.videoplayer.views.overviewCells

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel

@Composable
fun EmptyReview(
    accessViewModel: AccessViewModel
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.background
            )
    ) {

        if(accessViewModel.userIsInstructor.value){
            Text(
                text = stringResource(id = R.string.instructorEmptyReview),
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(20.dp)
            )
        }else{
            Text(
                text = stringResource(id = R.string.pilotEmptyReview),
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}