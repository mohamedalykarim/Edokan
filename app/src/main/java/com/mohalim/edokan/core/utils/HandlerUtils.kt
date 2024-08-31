package com.mohalim.edokan.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object HandlerUtils{

    fun saveBitmapToFile(bitmap: Bitmap, context: Context): Uri? {
        val fileName = "thumbnail_${System.currentTimeMillis()}.jpg"
        val file = File(context.cacheDir, fileName)
        return try {
            FileOutputStream(file).use { outStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}