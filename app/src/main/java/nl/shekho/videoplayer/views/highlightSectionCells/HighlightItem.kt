package nl.shekho.videoplayer.views.highlightSectionCells

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
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.helpers.extensions.Helpers
import nl.shekho.videoplayer.models.Event
import nl.shekho.videoplayer.ui.theme.*
import nl.shekho.videoplayer.viewModels.SessionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun HighlightItem(
    sessionViewModel: SessionViewModel,
    event: Event,
    isSelected: Boolean = false,
    activeHighlightColor: Color = lightBlue,
    inactiveColor: Color = highlightItemGray,
    onItemClick: () -> Unit,
) {

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
                        //Should be formatted
                        text = Helpers.convertSecondsToTime(event.timeStamp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(0.dp)
                            .background(deepBlue, RectangleShape)
                    )
                }

                Text(
                    text = sessionViewModel.altitude.toString(),
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
                painter = painterResource(id = sessionViewModel.getEventIcon(eventType = event.eventType)),
                contentDescription = "",
                tint = Color(sessionViewModel.getEventIconColor(eventType = event.eventType)),
                modifier = Modifier
                    .zIndex(4f)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(sessionViewModel.getEventIconColor(eventType = event.eventType)),
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
                ) {
                    Text(
                        text = sessionViewModel.getEventName(event.eventType),
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    ) {
                        //Feedback icon
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.1f)
                        ) {
                            if (!event.feedbackAll.isNullOrEmpty() || !event.feedbackFirstOfficer.isNullOrEmpty() || !event.feedbackCaptain.isNullOrEmpty()) {
                                Icon(
                                    painter = painterResource(id = R.drawable.feedback_logo),
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier
                                        .size(34.dp)
                                )
                            }
                        }

                        //Rating icon and number


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.9f)
                                .background(if(event.ratingAll != null) deepPurple else Color.Transparent)
                        ) {

                            if(event.ratingAll != null){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Text(
                                        text = event.ratingAll.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.primary
                                    )

                                    Icon(
                                        painter = painterResource(id = R.drawable.star_image_foreground),
                                        contentDescription = "",
                                        tint = starYellow,
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Divider(color = highlightDivider, thickness = 1.dp)
}


private fun formatDate(date: String): String {
    val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}