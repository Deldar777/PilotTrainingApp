package nl.shekho.videoplayer.views.cells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.views.VideoView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.SessionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FeedbackAndVideoSection(
    sessionViewModel: SessionViewModel
){
    val shape = RoundedCornerShape(20.dp)
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
                .weight(1.2f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.onBackground)
            ){

                //Note details section
                Box(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(),
                        horizontalAlignment = Alignment.Start
                        
                    ){

                        Text(
                            text = stringResource(id = R.string.addNote),
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )

                        Text(
                            text = stringResource(id = R.string.addNoteDescription),
                            color = MaterialTheme.colors.secondary,
                            fontWeight = FontWeight.Light,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "${stringResource(id = R.string.eventType)}: ",
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                            )

                            Icon(
                                painter = painterResource(id = sessionViewModel.selectedEvent.value.eventIcon),
                                contentDescription = "Feedback icon",
                                tint = Color(sessionViewModel.selectedEvent.value.eventIconColor),
                                modifier = Modifier.
                                        size(26.dp)
                            )

                            Text(
                                text = if(sessionViewModel.selectedEvent.value.eventType != null) sessionViewModel.selectedEvent.value.eventType!!.type else "Marked event",
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }



                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "${stringResource(id = R.string.altitude)}: ",
                                color = MaterialTheme.colors.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                            )
                            Text(
                                text = if(sessionViewModel.selectedEvent.value.altitude != null) sessionViewModel.selectedEvent.value.altitude.toString() else "Current altitude",
                                color = MaterialTheme.colors.secondary,
                                fontSize = 20.sp,
                            )
                        }


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "${stringResource(id = R.string.timestamp)}: ",
                                color = MaterialTheme.colors.primary,
                                fontSize = 20.sp,
                            )

                            val timestamp = sessionViewModel.selectedEvent.value.timestamp?.let {
                                formatDate(it)
                            } ?: run{
                                "Current timestamp"
                            }
                            Text(
                                text = timestamp,
                                color = MaterialTheme.colors.secondary,
                                fontSize = 20.sp,
                            )
                        }
                    }
                }

                //Add note section
                Box(
                    modifier = Modifier
                        .weight(2.5f)
                        .background(MaterialTheme.colors.background)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ){

                }



            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(date: String): String {
    val parsedDate = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
    return parsedDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
}