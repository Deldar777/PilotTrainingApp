package nl.shekho.videoplayer.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.selectedItemLightBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.overviewCells.SessionItems
import nl.shekho.videoplayer.views.topbarCells.TopBar
import kotlin.time.ExperimentalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
@Composable
fun OverviewView(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel
) {
    if (accessViewModel.loggedIn) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {

            Column {
                // Top bar
                TopBar(accessViewModel = accessViewModel)


                // Side bar and new session and review
                SessionsAndReview(
                    accessViewModel = accessViewModel,
                    sessionViewModel = sessionViewModel
                )
            }
        }
    } else {
        LoginView(accessViewModel = accessViewModel)
    }

}

@OptIn(ExperimentalTime::class)
@Composable
fun SessionsAndReview(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel
) {
    val shape = RoundedCornerShape(20.dp)

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
    ) {

        //sessions overview
        Box(
            modifier = Modifier
                .weight(0.75f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopCenter,
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f)

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    ) {
                        //Welcome text
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Welcome ${accessViewModel.userId}!",
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                            )
                        }
                        //Recent title
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Recent",
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2.0f)

                ) {
                    SessionItems(sessionViewModel = sessionViewModel)
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f)

                ) {
                    Box(
                        modifier = Modifier
                            .width(260.dp)
                            .height(60.dp)
                            .background(selectedItemLightBlue, shape = RoundedCornerShape(20.dp))
                            .clickable {

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_add_24),
                            contentDescription = "",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterStart),
                            tint = MaterialTheme.colors.primary
                        )

                        Text(
                            text = stringResource(id = R.string.newSession),
                            color = MaterialTheme.colors.primary,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        //New session and overview block
        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.background)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {

        }

    }
}