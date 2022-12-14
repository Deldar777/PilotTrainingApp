package nl.shekho.videoplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltAndroidApp
class PilotTrainingApp: Application(){
    companion object {
        var globalToken = ""
    }
}