package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.models.User
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.R

@Composable
fun NewSessionWindow(users: List<User>) {

    Box(
        modifier = Modifier
            .width(width = 650.dp)
            .height(height = 600.dp)
            .background(
                color = MaterialTheme.colors.onBackground
            )
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(tabBackground)
                .padding(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.newSession),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h4,
            )
        }

    }
}