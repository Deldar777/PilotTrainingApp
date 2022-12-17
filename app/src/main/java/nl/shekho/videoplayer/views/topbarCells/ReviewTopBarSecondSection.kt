package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.helpers.extensions.Helpers
import nl.shekho.videoplayer.ui.theme.lightPurple

@Composable
fun ReviewTopBarSecondSection(){

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        //Session name
        Text(
            text = stringResource(id = R.string.review),
            fontSize = 34.sp,
            color = lightPurple,
            fontWeight = FontWeight.Bold
        )

    }
}