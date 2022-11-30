package nl.shekho.videoplayer.views

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.textSecondaryDarkMode
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.generalCells.FeedbackMessage
import nl.shekho.videoplayer.views.navigation.Screens
import nl.shekho.videoplayer.views.topbarCells.TopBar
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun LoginView(
    accessViewModel: AccessViewModel,
    navController: NavController,
    sessionViewModel: SessionViewModel
) {

    if(accessViewModel.loggedIn){
        OverviewView(
            accessViewModel = accessViewModel,
            sessionViewModel = sessionViewModel,
            navController = navController
        )
    }else{
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            Column {
                // Top bar
                TopBar(accessViewModel = accessViewModel)
                // Login block
                LoginBox(
                    accessViewModel = accessViewModel,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun LoginBox(
    accessViewModel: AccessViewModel,
    navController: NavController
) {
    //Feedback color and message
    var messageColor by remember { mutableStateOf(Color.Red) }
    var messageText by remember { mutableStateOf("") }

    //Logon text fields
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //Feedback sentences
    val userNameOrPasswordEmpty = stringResource(id = R.string.emptyFields)
    val notInternet = stringResource(id = R.string.noInternet)
    val loginFailed = stringResource(id = R.string.loginFailed)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .padding(top = 80.dp)
    ) {
        Box(
            modifier = Modifier
                .width(width = 700.dp)
                .height(height = 500.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(
                    color = MaterialTheme.colors.onBackground
                )
        ) {
            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.vrefSolutions),
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepBlue,
                        modifier = Modifier.padding(top = 60.dp)
                    )
                }


                // Username textField
                OutlinedTextField(
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        textColor = MaterialTheme.colors.primaryVariant
                    ),
                    shape = RoundedCornerShape(20.dp),
                    value = username,
                    onValueChange = { username = it },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Bold
                    ),
                    label = {
                        Text(
                            text = stringResource(id = R.string.usernameExample),
                            color = textSecondaryDarkMode
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = stringResource(id = R.string.username),
                            tint = textSecondaryDarkMode
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp, start = 40.dp, end = 40.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                // Password textField
                OutlinedTextField(
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        textColor = MaterialTheme.colors.primaryVariant
                    ),
                    shape = RoundedCornerShape(20.dp),
                    value = password,
                    onValueChange = { password = it },
                    textStyle = TextStyle(
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Bold
                    ),
                    label = {
                        Text(
                            text = stringResource(id = R.string.enterPassword),
                            color = textSecondaryDarkMode
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = stringResource(id = R.string.password),
                            tint = textSecondaryDarkMode
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp, start = 40.dp, end = 40.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Login button
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {

                    OutlinedButton(
                        onClick = {
                            if (accessViewModel.isOnline()) {
                                if (username != "" && password != "") {

                                    accessViewModel.logIn(username, password)

                                    if (accessViewModel.succeeded.value) {
                                        navController.navigate(Screens.Overview.route)
                                    }else{
                                        messageText =loginFailed
                                    }
                                } else {
                                    messageText = userNameOrPasswordEmpty
                                }
                            } else {
                                messageText = notInternet
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = deepBlue,
                            contentColor = MaterialTheme.colors.primary
                        ),
                        modifier = Modifier
                            .width(300.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.login),
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
                FeedbackMessage(color = messageColor, text = messageText)
            }
        }

    }
}
