package nl.shekho.videoplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PilotTrainingApp: Application(){
    companion object {
        var globalToken = ""
    }
}