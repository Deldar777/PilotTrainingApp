package nl.shekho.videoplayer.helpers.contracts

import android.net.Uri
import nl.shekho.videoplayer.models.MetaData

interface MetaDataReaderInterface {
    fun getMetaDataFromUri(contentUri: Uri): MetaData?
}