package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime
import nl.shekho.videoplayer.views.generalCells.AlertDialog

@OptIn(ExperimentalTime::class)
@Composable
fun SessionTopBarThirdSection(
    sessionViewModel: SessionViewModel,
    navController: NavController,
    accessViewModel: AccessViewModel
) {
    OutlinedButton(
        onClick = {
            sessionViewModel.openDialog.value = true
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = deepPurple,
            contentColor = MaterialTheme.colors.primary,
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .width(200.dp)
            .height(50.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_exit_to_app_24),
            contentDescription = "",
            tint = MaterialTheme.colors.primary,
            modifier = Modifier
                .size(30.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.endSession),
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
        )
    }

    AlertDialog(
        sessionViewModel = sessionViewModel,
        accessViewModel = accessViewModel,
        navController = navController
    )
}

