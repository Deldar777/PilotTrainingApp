package nl.shekho.videoplayer.views.overviewCells

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun TextName(name: String, fontWeight: FontWeight, fontSize: Int){
    Text(
        text = name,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Center,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        color = MaterialTheme.colors.primaryVariant,
    )
}
