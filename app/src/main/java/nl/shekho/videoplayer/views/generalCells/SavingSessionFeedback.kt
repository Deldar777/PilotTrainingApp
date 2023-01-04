package nl.shekho.videoplayer.views.generalCells

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import nl.shekho.videoplayer.R
import nl.shekho.videoplayer.viewModels.SessionViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SavingSessionFeedback(
    sessionViewModel: SessionViewModel,
    context: Context,
){
    val saveChangesFailed = stringResource(id = R.string.saveChangesFailed)
    val saveChangesSucceeded = stringResource(id = R.string.saveChangesSucceeded)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        if(sessionViewModel.saveChangesAsked){
            if(sessionViewModel.savingChanges){
                Loading()
            }else{
                if (!sessionViewModel.saveChangesSucceeded) {
                    Toast.makeText(context, saveChangesFailed, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, saveChangesSucceeded, Toast.LENGTH_LONG).show()
                }
                sessionViewModel.saveChangesAsked = false
                sessionViewModel.saveChangesSucceeded = false
            }
        }
    }
}