package nl.shekho.videoplayer.views.cells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.highlightListGray

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HighlightItem(event: Event){

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {

        // Altitude and timestamp part
        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxHeight()
                .padding(),
            contentAlignment = Alignment.CenterStart,
        ){
            Text(
                text = "${event.altitude} ft",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 10.dp)
            )

        }

        // Icon part
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .padding(),
            contentAlignment = Alignment.Center,
        ){
            Icon(
                painter = painterResource(id = event.eventIcon),
                contentDescription = event.eventType.type,
                tint = Color(event.eventIconColor)
            )

        }

        // Event title part

        Box(
            modifier = Modifier
                .weight(2.0f)
                .fillMaxHeight()
                .padding(),
            contentAlignment = Alignment.Center,
        ){
            Text(
                text = event.eventType.type,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
    Divider(color = MaterialTheme.colors.primary, thickness = 1.dp)

}