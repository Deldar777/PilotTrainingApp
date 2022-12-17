package nl.shekho.videoplayer.views.topbarCells

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.customDarkGray
import nl.shekho.videoplayer.ui.theme.deepPurple
import nl.shekho.videoplayer.ui.theme.lightPurple
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TopBarReview(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel,
    accessViewModel: AccessViewModel,
    navController: NavController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(customDarkGray)
            .height(75.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.vref_logo_short_version),
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxSize()
                .background(customDarkGray)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                SessionTopBarFirstSection(
                    accessViewModel = accessViewModel,
                    sessionViewModel = sessionViewModel,
                    iconsColor = deepPurple,
                    titleColor = lightPurple
                )
            }

            // Second section
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .background(customDarkGray),
                contentAlignment = Alignment.Center
            ) {
                ReviewTopBarSecondSection()
            }

            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .background(customDarkGray)
                    .padding(end = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {

            }
        }
    }
}