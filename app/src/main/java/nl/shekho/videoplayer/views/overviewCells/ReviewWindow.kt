package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import nl.shekho.videoplayer.helpers.extensions.Helpers
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.navigation.Screens
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ReviewWindow(
    sessionViewModel: SessionViewModel,
    navController: NavController
) {

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
                    text = "${
                        sessionViewModel.selectedSession.value.startTime?.let {
                            Helpers.formatDateTimeSessionShort(
                                it
                            )
                        }
                    }",
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
                                    Text(
                                        text = stringResource(id = R.string.role),
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.primaryVariant,
                                    )

                                    Text(
                                        text = stringResource(id = R.string.instructor),
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.primaryVariant,
                                    )

                                    Text(
                                        text = stringResource(id = R.string.firstOfficer),
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.primaryVariant,
                                    )

                                    Text(
                                        text = stringResource(id = R.string.captain),
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.primaryVariant,
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
                                    Text(
                                        text = stringResource(id = R.string.name),
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.primaryVariant,
                                    )

                                    Text(
                                        text = "Andy Henson",
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.primaryVariant,
                                    )

                                    Text(
                                        text = "Daan Baer",
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.primaryVariant,
                                    )

                                    Text(
                                        text = "Lisa Bakke",
                                        fontFamily = FontFamily.Monospace,
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.primaryVariant,
                                    )
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
                                        navController.navigate(Screens.ReviewScreen.route)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.review),
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

