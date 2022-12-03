package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.models.User
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import nl.shekho.videoplayer.ui.theme.tabBackground
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.api.entities.UserEntity
import nl.shekho.videoplayer.viewModels.AccessViewModel
import nl.shekho.videoplayer.views.generalCells.ShowFeedback

@Composable
fun NewSessionWindow(accessViewModel: AccessViewModel) {

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

                Column {
                    accessViewModel.listUsers?.forEach { user ->

                        if(user.role == "ROLE_PILOT"){
                            Text(
                                text = "${user.firstname} ${user.lastname}",
                                style = MaterialTheme.typography.h5,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }


        ShowFeedback(text = accessViewModel.failed, color = Color.Red)

    }
}