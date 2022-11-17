package nl.shekho.videoplayer.helpers

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import nl.shekho.videoplayer.helpers.contracts.MetaDataReaderInterface
import nl.shekho.videoplayer.models.MetaData

class MetaDataReader(
    private val app: Application
): MetaDataReaderInterface {
    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {
        if(contentUri.scheme != "content"){
            return null
        }

        val fileName = app.contentResolver
            .query(
                contentUri,
                arrayOf(
                    MediaStore.Video.VideoColumns.DISPLAY_NAME,
                ),
                null,
                null,
                null,
            )
            ?.use {cursor ->
                val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(index)
            }
        return fileName?.let { fullFileName ->
            MetaData(
                fileName = Uri.parse(fullFileName).lastPathSegment ?: return null
            )
        }

    }
}