package nl.shekho.videoplayer.views.overviewCells

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.api.entities.UserEntity
import nl.shekho.videoplayer.models.Role
import nl.shekho.videoplayer.ui.theme.selectedItemLightBlue
import nl.shekho.videoplayer.ui.theme.textSecondaryDarkMode
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.views.generalCells.ShowFeedback

@Composable
fun NewSessionWindow(accessViewModel: AccessViewModel) {

    //session name text field
    var sessionName by remember { mutableStateOf("") }
    val users = accessViewModel.listUsers?.let { formatUsers(it) }


    //Dropdown participant 1
    var expanded1 by remember { mutableStateOf(false) }
    var participant1 by remember { mutableStateOf("") }
    var textfieldSize1 by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val icon1 = if (expanded1)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    //Dropdown participant 1
    var expanded2 by remember { mutableStateOf(false) }
    var participant2 by remember { mutableStateOf("") }
    var textfieldSize2 by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    val icon2 = if (expanded2)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown


    Box(
        modifier = Modifier
            .width(width = 650.dp)
            .height(height = 600.dp)
            .background(
                color = MaterialTheme.colors.onBackground
            )
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
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
                        .padding(bottom = 10.dp, top = 10.dp, start = 100.dp, end = 100.dp)
                ) {
                    // sessionName textField
                    OutlinedTextField(
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = MaterialTheme.colors.primaryVariant
                        ),
                        value = sessionName,
                        onValueChange = { sessionName = it },
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.primaryVariant,
                            fontWeight = FontWeight.Bold
                        ),
                        label = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(id = R.string.sessionName),
                                    color = Color.Black
                                )
                                Text(
                                    text = stringResource(id = R.string.enterSessionName),
                                    color = textSecondaryDarkMode
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    //Dropdown participant1
                    Column() {
                        OutlinedTextField(
                            colors = TextFieldDefaults.textFieldColors(
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
                                    modifier = Modifier
                                        .fillMaxWidth()
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
                                Icon(icon1,null,
                                    Modifier.clickable { expanded1 = !expanded1 },
                                    tint = textSecondaryDarkMode
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = expanded1,
                            onDismissRequest = { expanded1 = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current){textfieldSize1.width.toDp()})
                        ) {
                            users?.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    participant1 = label
                                }) {
                                    Text(text = label)
                                }
                            }
                        }
                    }

                    //Consent participant 1
                    Consent()

                    //Dropdown participant2
                    Column() {
                        OutlinedTextField(
                            colors = TextFieldDefaults.textFieldColors(
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
                                    modifier = Modifier
                                        .fillMaxWidth()
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
                                Icon(icon2,null,
                                    Modifier.clickable { expanded2 = !expanded2 },
                                    tint = textSecondaryDarkMode
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current){textfieldSize2.width.toDp()})
                        ) {
                            users?.forEach { label ->
                                DropdownMenuItem(onClick = {
                                    participant2 = label
                                }) {
                                    Text(text = label)
                                }
                            }
                        }
                    }

                    //Consent participant 2
                    Consent()

                    //Start button
                    Box(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .width(260.dp)
                            .height(60.dp)
                            .background(selectedItemLightBlue, shape = RoundedCornerShape(20.dp))
                            .clickable {

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.start),
                            color = MaterialTheme.colors.primary,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        ShowFeedback(text = accessViewModel.failed, color = Color.Red)
    }
}

fun formatUsers(users: List<UserEntity>): List<String> {
    val formattedUsers = mutableListOf<String>()

    users.forEach { user ->
        if (user.role == Role.PILOT.type) {
            formattedUsers += "${user.firstname} ${user.lastname}"
        }
    }
    return formattedUsers
}
