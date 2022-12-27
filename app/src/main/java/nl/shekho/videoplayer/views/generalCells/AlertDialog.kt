package nl.shekho.videoplayer.views.generalCells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.views.navigation.Screens

@OptIn(ExperimentalTime::class)
@Composable
fun AlertDialog(
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel,
    navController: NavController
) {
    val openDialog = sessionViewModel.openDialog


    if (openDialog.value) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnClickOutside = false
            ),
            onDismissRequest = { openDialog.value = false },
            text = {
                Text(
                    text = stringResource(id = R.string.endSessionAlert),
                    color = MaterialTheme.colors.primary,
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        sessionViewModel.saveSessionAsked = true
                        sessionViewModel.runningSession!!.id?.let {
                            sessionViewModel.endSession(
                                sessionId = it,
                                token = accessViewModel.encodedJwtToken!!
                            )
                        }
                    }) {
                    Text(
                        text = stringResource(id = R.string.confirm),
                        color = lightBlue,
                        fontSize = 24.sp
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = lightBlue,
                        fontSize = 24.sp
                    )
                }
            },
            backgroundColor = customDarkGray,
            contentColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .height(160.dp)
                .width(260.dp)
        )
    }



}
