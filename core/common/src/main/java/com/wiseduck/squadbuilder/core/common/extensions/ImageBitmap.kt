package com.wiseduck.squadbuilder.core.common.extensions

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.File
import java.io.FileOutputStream

fun ImageBitmap.saveToDisk(
    context: Context,
    baseName: String,
): String {
    val fileName = "${baseName}_${System.currentTimeMillis()}.png"
    val cachePath = File(context.cacheDir, "shared_images")
    cachePath.mkdirs()
    val file = File(cachePath, fileName)
    val outputStream = FileOutputStream(file)

    asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return file.absolutePath
}
