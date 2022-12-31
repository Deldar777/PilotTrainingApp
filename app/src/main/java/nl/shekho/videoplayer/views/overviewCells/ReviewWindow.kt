package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nl.shekho.videoplayer.helpers.extensions.Helpers
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.NoInternetView
import nl.shekho.videoplayer.views.generalCells.ShowFeedback
import nl.shekho.videoplayer.views.navigation.Screens
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ReviewWindow(
    sessionViewModel: SessionViewModel,
    navController: NavController,
    accessViewModel: AccessViewModel
) {

    //Get users for the selected session
    val users by sessionViewModel.users.collectAsState()
    val reviewSession = stringResource(id = R.string.review)
    val continueSession = stringResource(id = R.string.continueProcess)

    Box(
        modifier = Modifier
            .width(width = 650.dp)
            .height(height = 600.dp)
            .background(
                color = MaterialTheme.colors.onBackground
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Session title section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.35f)
                    .background(tabBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sessionViewModel.selectedSession.value.startTime.let {
                        Helpers.formatDateTimeSessionShort(
                            it
                        )
                    },
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding()
                )
            }


            // Participants section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(top = 60.dp, start = 30.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.participants),
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding()
                        )

                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopStart
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            // Roles section
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1.5f)
                                    .fillMaxHeight()
                                    .padding(top = 60.dp),
                                contentAlignment = Alignment.TopStart
                            ) {

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                    //Roles
                                    TextName(
                                        name = stringResource(id = R.string.role),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16
                                    )
                                    TextName(
                                        name = stringResource(id = R.string.instructor),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16
                                    )

                                    TextName(
                                        name = stringResource(id = R.string.firstOfficer),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16
                                    )

                                    TextName(
                                        name = stringResource(id = R.string.captain),
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16
                                    )
                                }
                            }

                            // Names section
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f)
                                    .padding(top = 60.dp)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.TopStart

                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                }

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    TextName(
                                        name = stringResource(id = R.string.name),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16
                                    )

                                    if (sessionViewModel.isOnline()) {

                                        if (accessViewModel.encodedJwtToken != null && sessionViewModel.selectedSession.value.id != null) {
                                            sessionViewModel.fetchUsersBySessionId(
                                                sessionId = sessionViewModel.selectedSession.value.id!!,
                                                token = accessViewModel.encodedJwtToken!!
                                            )
                                        }

                                        users?.let { users ->
                                            users
                                                .onSuccess {

                                                    val instructor = it[0]
                                                    val firstOfficer = it[1]
                                                    val captain = it[2]

                                                    TextName(
                                                        name = instructor.fullName,
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = 14
                                                    )

                                                    TextName(
                                                        name = firstOfficer.fullName,
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = 14
                                                    )
                                                    accessViewModel.firstOfficer = firstOfficer

                                                    TextName(
                                                        name = captain.fullName,
                                                        fontWeight = FontWeight.Normal,
                                                        fontSize = 14
                                                    )
                                                    accessViewModel.captain = captain
                                                }
                                                .onFailure {
                                                    ShowFeedback(
                                                        text = stringResource(id = R.string.generalError),
                                                        color = Color.Red
                                                    )
                                                }

                                        } ?: run {
                                            CircularProgressIndicator(color = Color.White)
                                        }
                                    } else {
                                        NoInternetView()
                                    }


                                }
                            }

                        }
                    }
                }
            }


            // Session section

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(top = 30.dp, start = 30.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = stringResource(id = R.string.session),
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding()
                        )

                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .fillMaxHeight()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.session_image),
                                contentDescription = stringResource(id = R.string.session),
                                modifier = Modifier
                                    .height(180.dp)
                                    .width(250.dp)
                                    .padding(top = 30.dp)
                            )
                        }

                    }
                }
            }

            // Review button section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.40f)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .fillMaxHeight()
                    ) {

                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .fillMaxHeight()
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(260.dp)
                                    .height(60.dp)
                                    .background(lightBlue, shape = RoundedCornerShape(20.dp))
                                    .clickable {

                                        if (sessionViewModel.isSessionStillRunning()) {
                                            sessionViewModel.runningSession =
                                                sessionViewModel.selectedSession.value
                                            navController.navigate(Screens.SessionScreen.route)
                                        } else {
                                            sessionViewModel.runningSession =
                                                sessionViewModel.selectedSession.value
                                            navController.navigate(Screens.ReviewScreen.route)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if(sessionViewModel.isSessionStillRunning()) continueSession else reviewSession ,
                                    color = MaterialTheme.colors.primary,
                                    textAlign = TextAlign.Center,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


