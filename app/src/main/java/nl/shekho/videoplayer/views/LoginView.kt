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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.views.highlightSectionCells.NavigationBar

@Composable
fun LoginView(context: Context) {

    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ){

        Column {

            // Top bar
            NavigationBar()

            // Login block
            LoginBox(context = context)
        }
    }
}


@Composable
fun LoginBox(context: Context){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .padding(top = 80.dp)
    ){
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
                        text = "VRef solutions",
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = deepBlue,
                    )
                }


                // Email textField
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "example@vrefsolutions.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Email")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp, start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                // Password textField
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Enter your password") },
                    leadingIcon = {
                        Icon(Icons.Default.Info, contentDescription = "Password")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp, start = 20.dp, end = 20.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                // Reset password and login button
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {

                    Text(
                        text = "Reset password",
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(textDecoration = TextDecoration.Underline),
                        color = deepBlue,
                        modifier = Modifier
                            .clickable {

                            }
                    )

                    OutlinedButton(
                        onClick = {
                            login(email, password, context)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = deepPurple,
                            contentColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Text(
                            text = "Marked event",
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

    }
}

fun login(email: String, password: String, context: Context) {

    if (email == "simba" && password == "12345") {
        Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Logged In Failed", Toast.LENGTH_SHORT).show()
    }
}