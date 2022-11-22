package nl.shekho.videoplayer.views.noteCells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.views.VideoView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.SessionViewModel
import nl.shekho.videoplayer.views.noteCells.FeedbackSection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FeedbackAndVideoSection(
    sessionViewModel: SessionViewModel
){
    val shape = RoundedCornerShape(20.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {

        // Video section
        Box(
            modifier = Modifier
                .weight(2f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            VideoView()
        }

        //Feedback section
        Box(
            modifier = Modifier
                .weight(1.2f)
                .background(MaterialTheme.colors.onBackground, shape)
                .fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            FeedbackSection(sessionViewModel = sessionViewModel)
        }
    }
}


