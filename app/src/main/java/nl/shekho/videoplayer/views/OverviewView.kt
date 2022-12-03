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
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.selectedItemLightBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.navigation.Screens
import nl.shekho.videoplayer.views.overviewCells.NewSessionButton
import nl.shekho.videoplayer.views.overviewCells.NewSessionWindow
import nl.shekho.videoplayer.views.overviewCells.ReviewWindow
import nl.shekho.videoplayer.views.overviewCells.SessionItems
import nl.shekho.videoplayer.views.topbarCells.TopBar
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun OverviewView(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController
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
                    sessionViewModel = sessionViewModel,
                    navController = navController
                )
            }
        }
    } else {
        LoginView(
            accessViewModel = accessViewModel,
            navController = navController,
            sessionViewModel = sessionViewModel
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun SessionsAndReview(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController
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
                        .weight(0.5f)

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        //Welcome text
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Welcome Andy!",
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
                                text = stringResource(id = R.string.recent),
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
                    if(accessViewModel.userIsInstructor.value){
                        NewSessionButton(sessionViewModel = sessionViewModel)
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

            if (sessionViewModel.showNewSessionWindow.value && accessViewModel.userIsInstructor.value) {
                NewSessionWindow(accessViewModel = accessViewModel)
            }

            if (sessionViewModel.showReviewWindow.value) {
                ReviewWindow(session = sessionViewModel.selectedSession.value)
            }

        }

    }
}