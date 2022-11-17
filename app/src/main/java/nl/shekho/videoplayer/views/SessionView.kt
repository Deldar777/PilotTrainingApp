package nl.shekho.videoplayer.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.ui.theme.highlightListGray
import nl.shekho.videoplayer.views.cells.NavigationBar

@Composable
fun SessionView() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Column {

            // Top bar
            NavigationBar()

            // Middle part of the screen (highlight - video and add feedback block)
            HighlightAndVideo()


        }
    }
}


@Composable
fun HighlightAndVideo() {

    val shape = RoundedCornerShape(20.dp)
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
    ) {


        //Highlight section
        Box(
            modifier = Modifier
                .weight(0.75f)
                .background(highlightListGray, shape)
                .fillMaxHeight()
                .padding(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Text(
                text = "Highlight",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }

        //Video and feedback section
        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.background)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {

                // Video section
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .background(MaterialTheme.colors.onBackground, shape)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    VideoView()
                }

                //Feedback section
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colors.onBackground, shape)
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Feedback")
                }

            }
        }

    }
}
