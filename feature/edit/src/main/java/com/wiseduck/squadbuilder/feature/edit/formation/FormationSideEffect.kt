package com.wiseduck.squadbuilder.feature.edit.formation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun FormationSideEffects(
    state: FormationUiState,
    formationGraphicsLayer: GraphicsLayer
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.sideEffect) {
        state.sideEffect?.let { effect ->
            when (effect) {
                is FormationSideEffect.CaptureFormation -> {
                    scope.launch {
                        val uri = captureFormationAndGetUri(
                            context = context,
                            graphicsLayer = formationGraphicsLayer,
                            quarter = effect.quarter
                        )

                        effect.onCaptureUri(effect.quarter, uri)
                    }
                }

                is FormationSideEffect.ShareMultipleImages -> {
                    context.shareMultipleImages(effect.imageUris)
                }
            }
        }
    }
}

private suspend fun captureFormationAndGetUri(
    context: Context,
    graphicsLayer: GraphicsLayer,
    quarter: Int
): Uri? {
    return try {
        val imageBitmap = graphicsLayer.toImageBitmap()

        val filePath = imageBitmap.saveToDisk(context, "formation_q${quarter}")

        val file = File(filePath)
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun Context.shareMultipleImages(imageUris: List<Uri>) {
    if (imageUris.isEmpty()) return

    try {
         val builder = ShareCompat.IntentBuilder(this)
             .setType("image/*")

         imageUris.forEach { builder.addStream(it) }

         val intent = builder.intent.apply {
             action = android.content.Intent.ACTION_SEND_MULTIPLE

             addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
         }

         startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun ImageBitmap.saveToDisk(context: Context, baseName: String) : String {
    val fileName = "${baseName}_${System.currentTimeMillis()}.png"
    val cachePath = File(context.cacheDir, "shared_images")
    cachePath.mkdirs()
    val file = File(cachePath, fileName)
    val outputStream = FileOutputStream(file)

    asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    return  file.absolutePath
}