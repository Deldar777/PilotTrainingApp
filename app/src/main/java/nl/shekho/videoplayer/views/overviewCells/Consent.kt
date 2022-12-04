package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.textSecondaryDarkMode

@Composable
fun Consent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_check_box_24),
            contentDescription = null,
            tint = textSecondaryDarkMode,
        )

        Text(
            text = stringResource(id = R.string.consent),
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}