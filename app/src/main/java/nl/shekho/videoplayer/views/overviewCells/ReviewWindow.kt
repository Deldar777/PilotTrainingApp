package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.models.Session
import nl.shekho.videoplayer.models.User
import nl.shekho.videoplayer.ui.theme.tabBackground
import java.time.LocalDateTime

@Composable
fun ReviewWindow(session: Session) {

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
                text = "${session.company?.name} - ${session.startTime?.let { formatDate(it) }}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h4,
            )
        }
    }
}


private fun formatDate(date: LocalDateTime): String {
    return "${date.dayOfWeek.toString().lowercase().subSequence(0,3)} ${date.dayOfMonth}th"
}