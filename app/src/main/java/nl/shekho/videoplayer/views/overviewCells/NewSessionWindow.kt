package nl.shekho.videoplayer.views.overviewCells

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.api.entities.NewSessionEntity
import nl.shekho.videoplayer.api.entities.VideoRequestEntity
import nl.shekho.videoplayer.models.Role
import nl.shekho.videoplayer.ui.theme.textSecondaryDarkMode
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.helpers.extensions.Helpers
import nl.shekho.videoplayer.models.User
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.views.generalCells.Loading
import nl.shekho.videoplayer.views.navigation.Screens

@OptIn(ExperimentalTime::class)
@Composable
fun NewSessionWindow(
    accessViewModel: AccessViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    context: Context
) {

    //Feedback variables
    val noInternet = stringResource(id = R.string.noInternet)
    val sessionCreationError = stringResource(id = R.string.sessionCreationError)
    val noToken = stringResource(id = R.string.noToken)

    //session name text field
    var sessionName by remember { mutableStateOf("") }
    val mappedUsers = mapUsers(accessViewModel.listUsers)


    //Dropdown participant 1
    var expanded1 by remember { mutableStateOf(false) }
    var participant1 by remember { mutableStateOf("") }
    var textfieldSize1 by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val icon1 = if (expanded1) Icons.Filled.ArrowDropUp
    else Icons.Filled.ArrowDropDown

    //Dropdown participant 1
    var expanded2 by remember { mutableStateOf(false) }
    var participant2 by remember { mutableStateOf("") }
    var textfieldSize2 by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val icon2 = if (expanded2) Icons.Filled.ArrowDropUp
    else Icons.Filled.ArrowDropDown


    Box(
        modifier = Modifier
            .width(width = 650.dp)
            .height(height = 600.dp)
            .background(
                color = MaterialTheme.colors.onBackground
            )
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
                    .background(MaterialTheme.colors.onBackground),
                contentAlignment = Alignment.TopCenter
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(tabBackground)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.newSession),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.h4,
                    )
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2.5f),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 10.dp, top = 10.dp, start = 80.dp, end = 100.dp)
                ) {

                    // sessionName textField
                    sessionName = Helpers.getSessionName()
                    OutlinedTextField(
                        enabled = false, colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = MaterialTheme.colors.primaryVariant
                        ), value = sessionName, onValueChange = { }, textStyle = TextStyle(
                            color = MaterialTheme.colors.primaryVariant,
                            fontWeight = FontWeight.Bold
                        ), label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.sessionDate),
                                    color = Color.Black
                                )
                                Text(
                                    text = stringResource(id = R.string.sessionDate),
                                    color = textSecondaryDarkMode
                                )
                            }
                        }, modifier = Modifier.fillMaxWidth()
                    )

                    //Dropdown participant1
                    Column() {
                        OutlinedTextField(colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = MaterialTheme.colors.primaryVariant
                        ),
                            value = participant1,
                            onValueChange = { participant1 = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    //This value is used to assign to the DropDown the same width
                                    textfieldSize1 = coordinates.size.toSize()
                                },
                            label = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.firstOfficer),
                                        color = Color.Black
                                    )
                                    Text(
                                        text = stringResource(id = R.string.selectParticipant1),
                                        color = textSecondaryDarkMode
                                    )
                                }
                            },
                            trailingIcon = {
                                Icon(
                                    icon1,
                                    null,
                                    Modifier.clickable { expanded1 = !expanded1 },
                                    tint = textSecondaryDarkMode
                                )
                            })
                        DropdownMenu(
                            expanded = expanded1,
                            onDismissRequest = { expanded1 = false },
                            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize1.width.toDp() })
                        ) {
                            mappedUsers.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    participant1 = label.key
                                }) {
                                    Text(text = label.key)
                                }
                            }
                        }
                    }

                    //Consent participant 2
                    Consent()

                    //Dropdown participant2
                    Column() {
                        OutlinedTextField(colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = MaterialTheme.colors.primaryVariant
                        ),
                            value = participant2,
                            onValueChange = { participant2 = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    //This value is used to assign to the DropDown the same width
                                    textfieldSize2 = coordinates.size.toSize()
                                },
                            label = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.captain),
                                        color = Color.Black
                                    )
                                    Text(
                                        text = stringResource(id = R.string.selectParticipant2),
                                        color = textSecondaryDarkMode
                                    )
                                }
                            },
                            trailingIcon = {
                                Icon(
                                    icon2,
                                    null,
                                    Modifier.clickable { expanded2 = !expanded2 },
                                    tint = textSecondaryDarkMode
                                )
                            })
                        DropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false },
                            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize2.width.toDp() })
                        ) {
                            mappedUsers.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    participant2 = label.key
                                }) {
                                    Text(text = label.key)
                                }
                            }
                        }
                    }

                    //Consent participant 2
                    Consent()

                    //Session start button
                    // Login button
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {

                        OutlinedButton(
                            enabled = !sessionViewModel.loading.value && participant1.isNotEmpty() && participant2.isNotEmpty() && participant1 != participant2,
                            onClick = {
                                if (accessViewModel.isOnline()) {
                                    val instructorId = accessViewModel.loggedInUserId
                                    val participant1Id = mappedUsers[participant1]?.id
                                    val participant2Id = mappedUsers[participant2]?.id
                                    val companyId = accessViewModel.companyId
                                    val token = accessViewModel.encodedJwtToken

                                    val newSessionEntity = NewSessionEntity(
                                        UserIds = listOf(
                                            instructorId, participant1Id, participant2Id
                                        ), CompanyId = companyId
                                    )

                                    if (token != null) {
                                        sessionViewModel.createSessionAsked = true
                                        sessionViewModel.token.value = token
                                        sessionViewModel.createSession(newSessionEntity, token)
                                        accessViewModel.firstOfficer = mappedUsers[participant1]
                                        accessViewModel.captain = mappedUsers[participant2]
                                    } else {
                                        Toast.makeText(context, noToken, Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(context, noInternet, Toast.LENGTH_LONG).show()
                                }

                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = lightBlue,
                                contentColor = MaterialTheme.colors.primary
                            ),
                            modifier = Modifier.width(300.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.start),
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }


                }
            }
            if (sessionViewModel.createSessionAsked) {
                if (sessionViewModel.loading.value) {
                    Loading()
                } else {
                    if (!sessionViewModel.succeeded) {
                        Toast.makeText(context, sessionCreationError, Toast.LENGTH_LONG).show()
                    } else {
                        navController.navigate(Screens.SessionScreen.route)
                    }
                    sessionViewModel.createSessionAsked = false
                }
            }
        }
    }
}

fun mapUsers(users: List<User?>): MutableMap<String, User> {
    val mappedUsers = mutableMapOf<String, User>()

    users.forEach { user ->
        if (user != null) {
            if (user.role == Role.PILOT.type) {
                mappedUsers["${user.firstName} ${user.lastName}"] = user
            }
        }
    }
    return mappedUsers
}
