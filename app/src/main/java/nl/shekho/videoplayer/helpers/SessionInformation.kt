package nl.shekho.videoplayer.helpers

import android.media.session.MediaSession.Token

object SessionInformation {
    const val JWTTOKEN = "token"
    var sessionToken = ""
    const val NAME = "name"
    const val PARTICIPANT1_NAME = "participant1Name"
    const val PARTICIPANT2_NAME = "participant2Name"
    const val COMPANYID = "companyId"
}