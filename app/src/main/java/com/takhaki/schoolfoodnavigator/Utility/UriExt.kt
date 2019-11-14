package com.takhaki.schoolfoodnavigator.Utility

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.net.toFile

fun Uri.getFileName(context: Context): String? {
    var fileName: String? = null
    when (scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
            val cursor = context.contentResolver
                .query(this, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                if (index >= 0) fileName = cursor.getString(index)
            }
            cursor?.close()
        }

        ContentResolver.SCHEME_FILE -> fileName = toFile().name

        else -> Unit
    }
    return fileName
}

fun Uri.getMimeType(context: Context): String? {
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> context.contentResolver.getType(this)

        ContentResolver.SCHEME_FILE -> MimeTypeMap.getFileExtensionFromUrl(toString())?.let {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(it)
        }

        else -> null
    }
}

fun Uri.getFileSize(context: Context): Long? {
    var fileSize: Long? = null
    when (scheme) {
        ContentResolver.SCHEME_CONTENT -> {
            val projection = arrayOf(MediaStore.MediaColumns.SIZE)
            val cursor = context.contentResolver
                .query(this, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)
                if (index >= 0) fileSize = cursor.getLong(index)
            }
            cursor?.close()
        }

        ContentResolver.SCHEME_FILE -> fileSize = toFile().length()

        else -> Unit
    }
    return fileSize
}

fun Uri.exists(context: Context): Boolean {
    return (getFileSize(context) ?: 0) > 0
}