package nl.shekho.videoplayer.views.cells

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.ui.theme.deepBlue

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(customDarkGray)
            .padding(15.dp)
    ) {
        Text(
            text = "VRef solution",
            color = deepBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(customDarkGray)
                .padding(15.dp)

        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "First section",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Second section",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Third section",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )

            }
        }
    }
}