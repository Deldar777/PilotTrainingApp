package nl.shekho.videoplayer.ui.theme.pilotTrainingThemes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import nl.shekho.videoplayer.ui.theme.mediumGray
import nl.shekho.videoplayer.ui.theme.textSecondary


private val customDarkColor = darkColors(
    primary = Color.White,
    secondary = textSecondary,
    background = Color.DarkGray,
    onBackground = mediumGray,
)


private val customLightColor = darkColors(
    primary = Color.Black,
    secondary = Color.Gray,
    background = Color.White
)

@Composable
fun PilotTrainingTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val customColors = if(darkTheme) customDarkColor
    else customLightColor

    MaterialTheme(
        colors = customColors,
        content = content
    )
}