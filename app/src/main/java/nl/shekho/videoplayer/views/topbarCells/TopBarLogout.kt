package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.ui.theme.deepBlue
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.helpers.SessionInformation
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.navigation.Screens
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TopBarLogout(
    accessViewModel: AccessViewModel,
    navController: NavController,
    sessionViewModel: SessionViewModel
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(customDarkGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.vref_logo),
            contentDescription = "",
            modifier = Modifier
                .width(200.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(customDarkGray)
                .padding(15.dp)

        ) {
            Box(
                modifier = Modifier
                    .background(customDarkGray)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        color = deepBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        modifier = Modifier.clickable {
                            accessViewModel.resetSessionInformation()
                            sessionViewModel.resetViewWindowsValues()
                        }
                    )
                }
            }
        }

    }
}