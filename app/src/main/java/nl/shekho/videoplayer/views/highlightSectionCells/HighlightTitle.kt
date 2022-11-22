package nl.shekho.videoplayer.views.highlightSectionCells

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R

@Composable
fun HighlightTitle(){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.highlightTitle),
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    }
}