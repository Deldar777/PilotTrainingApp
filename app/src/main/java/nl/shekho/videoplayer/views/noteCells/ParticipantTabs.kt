package nl.shekho.videoplayer.views.noteCells

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.ui.theme.lightBlue
import nl.shekho.videoplayer.ui.theme.tabBackground
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ParticipantTabs(
    hasFeedback: Boolean,
    tabName: String,
    isSelected: Boolean = false,
    activeHighlightColor: Color = lightBlue,
    inactiveColor: Color = tabBackground,
    onItemClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)
            .border(BorderStroke(0.1.dp, Color.Gray))
            .clickable {
                onItemClick()
            }
            .background(
                if (isSelected) activeHighlightColor
                else inactiveColor
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1.4f)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = tabName,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }

            if (hasFeedback) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.feedback_logo),
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }
    }
}