package nl.shekho.videoplayer.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.overviewCells.*
import nl.shekho.videoplayer.views.topbarCells.TopBarLogout
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun OverviewView(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    context: Context
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        OverviewWindow(
            accessViewModel = accessViewModel,
            sessionViewModel = sessionViewModel,
            navController = navController,
            context = context
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun OverviewWindow(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    context: Context
) {
    val shape = RoundedCornerShape(20.dp)

    Column {
        // Top bar
        TopBarLogout(
            accessViewModel = accessViewModel,
            sessionViewModel = sessionViewModel,
            navController = navController
        )

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
                                    text = "${stringResource(id = R.string.welcome)} ${accessViewModel.loggedInUser?.firstName ?: ""}",
                                    fontFamily = FontFamily.Monospace,
                                    textAlign = TextAlign.Center,
                                    fontSize = 22.sp,
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
                                    fontSize = 18.sp,
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
                        SessionItems(
                            sessionViewModel = sessionViewModel,
                            accessViewModel = accessViewModel
                        )
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)

                    ) {
                        if (accessViewModel.userIsInstructor.value) {
                            NewSessionButton(
                                sessionViewModel = sessionViewModel
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

                if (sessionViewModel.showNewSessionWindow.value) {
                    NewSessionWindow(
                        accessViewModel = accessViewModel,
                        sessionViewModel = sessionViewModel,
                        navController = navController,
                        context = context
                    )
                }

                if (sessionViewModel.showReviewWindow.value) {
                    ReviewWindow(
                        sessionViewModel = sessionViewModel,
                        navController = navController,
                        accessViewModel = accessViewModel
                    )
                }

                if (sessionViewModel.showEmptyReview.value) {
                    EmptyReview(accessViewModel = accessViewModel)
                }

            }

        }
    }
}