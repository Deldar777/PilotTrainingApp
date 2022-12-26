package nl.shekho.videoplayer.views.generalCells

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
                        sessionViewModel.endSession()
                        openDialog.value = false
                        navController.navigate(Screens.OverviewScreen.route)
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
            backgroundColor = MaterialTheme.colors.onBackground,
            contentColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .height(160.dp)
                .width(260.dp)
        )
    }

}
