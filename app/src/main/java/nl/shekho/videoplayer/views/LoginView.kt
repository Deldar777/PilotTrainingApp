package nl.shekho.videoplayer.views

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.ui.theme.textSecondaryDarkMode
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.views.generalCells.FeedbackMessage
import nl.shekho.videoplayer.views.generalCells.Loading
import nl.shekho.videoplayer.views.navigation.Screens
import nl.shekho.videoplayer.views.topbarCells.TopBarLogin


@Composable
fun LoginView(
    accessViewModel: AccessViewModel,
    context: Context,
    navController: NavController
){
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        LoginWindow(
            accessViewModel = accessViewModel,
            context = context,
            navController = navController
        )
    }
}

@Composable
fun LoginWindow(
    accessViewModel: AccessViewModel,
    context: Context,
    navController: NavController
) {
    //Login text fields
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //Feedback sentences
    val notInternet = stringResource(id = R.string.noInternet)
    val loginFailed = stringResource(id = R.string.loginFailed)

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        // Top bar
        TopBarLogin()
        // Login block
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
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.vref_logo_medium_gray),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .width(500.dp)
                                .height(120.dp)
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
                            enabled = !accessViewModel.loading && (username.isNotEmpty() && password.isNotEmpty()),
                            onClick = {
                                if (accessViewModel.isOnline()) {
                                    accessViewModel.loginAsked = true
                                    accessViewModel.logIn(username, password)
                                } else {
                                    Toast.makeText(context, notInternet, Toast.LENGTH_LONG).show()
                                }
                            },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = lightBlue,
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

                    if(accessViewModel.loginAsked){
                        if (accessViewModel.loading) {
                            Loading()
                        } else {
                            if (!accessViewModel.succeeded) {
                                Toast.makeText(context, loginFailed, Toast.LENGTH_LONG).show()
                            } else {
                                navController.navigate(Screens.OverviewScreen.route)
                            }
                            accessViewModel.loginAsked = false
                        }
                    }
                }
            }

        }
    }
}
