package nl.shekho.videoplayer.views.highlightSectionCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HighlightItem(
    event: Event,
    isSelected: Boolean = false,
    activeHighlightColor: Color = selectedItemLightBlue,
    inactiveColor: Color = highlightItemGray,
    onItemClick: () -> Unit
) {

    var showFeedbackIcon by remember {
        mutableStateOf(event.hasFeedback)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                onItemClick()
            }
            .background(
                if (isSelected) activeHighlightColor
                else inactiveColor
            )
    ) {


        // Altitude and timestamp part
        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxHeight()
                .padding(),
            contentAlignment = Alignment.CenterStart,
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 2.dp)
                ) {
                    Text(
                        text = event.timestamp?.let { formatDate(it) } ?: "",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(0.dp)
                            .background(deepBlue, RectangleShape)
                    )
                }

                Text(
                    text = "${event.altitude} ft",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }


        // Icon part
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight()
                .padding(),
            contentAlignment = Alignment.Center,
        ) {
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(1.dp)
                    .zIndex(1f)
            )

            Icon(
                painter = painterResource(id = event.eventIcon),
                contentDescription = event.eventType?.type,
                tint = Color(event.eventIconColor),
                modifier = Modifier
                    .zIndex(4f)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(event.eventIconColor),
                        shape = CircleShape
                    )
                    .padding(4.dp)
            )

        }


        // Event title and feedback part
        Box(
            modifier = Modifier
                .weight(2.0f)
                .fillMaxHeight()
                .padding(start = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .weight(2.0f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart,
                ){
                    event.eventType?.let {
                        Text(
                            text = it.type,
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart,
                ){
                    if(showFeedbackIcon){
                        Icon(
                            painter = painterResource(id = event.feedbackIcon),
                            contentDescription = "",
                            tint = MaterialTheme.colors.primary,
                        )
                    }
                }
            }

        }
    }

    Divider(color = highlightDivider, thickness = 1.dp)
}


@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(date: String): String {
    val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}