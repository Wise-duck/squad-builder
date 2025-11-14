package com.wiseduck.squadbuilder.feature.edit.formation

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

@Composable
fun FormationSideEffects(
    state: FormationUiState,
    formationGraphicsLayer: GraphicsLayer
) {
    val context = LocalContext.current

    LaunchedEffect(state.sideEffect) {
        when (state.sideEffect) {
            is FormationSideEffect.ShareFormation -> {
                context.shareForImageBitmap(imageBitmap = state.sideEffect.imageBitmap)
            }

            else -> { }
        }
    }

    LaunchedEffect(state.isFormationSharing) {
        if (state.isFormationSharing) {
            state.eventSink(FormationUiEvent.ShareFormation(formationGraphicsLayer.toImageBitmap()))
        }
    }
}

private fun Context.shareForImageBitmap(imageBitmap: ImageBitmap) {
    try {
        val file = File(imageBitmap.saveToDisk(this))
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)

        ShareCompat.IntentBuilder(this)
            .setStream(uri)
            .setType("image/png")
            .startChooser()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun ImageBitmap.saveToDisk(context: Context) : String {
    val fileName = "formation_shared_image_${System.currentTimeMillis()}.png"
    val cachePath = File(context.cacheDir, "shared_images")
    cachePath.mkdirs()
    val file = File(cachePath, fileName)
    val outputStream = FileOutputStream(file)

    asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return  file.absolutePath
}