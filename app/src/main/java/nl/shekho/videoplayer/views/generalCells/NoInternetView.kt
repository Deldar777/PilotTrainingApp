package nl.shekho.videoplayer.views.generalCells

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R

@Composable
fun NoInternetView() {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_wifi_off_24),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(60.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = stringResource(id = R.string.noInternetHighlight),
                color = MaterialTheme.colors.secondary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
