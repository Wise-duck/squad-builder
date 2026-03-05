package com.wiseduck.squadbuilder.feature.edit.formation

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.wiseduck.squadbuilder.core.common.extensions.saveToDisk
import com.wiseduck.squadbuilder.core.common.extensions.shareImages
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun HandleFormationSideEffect(
    state: FormationUiState,
    formationGraphicsLayer: GraphicsLayer,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.sideEffect) {
        val effect = state.sideEffect ?: return@LaunchedEffect

        when (effect) {
            is FormationSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }

            is FormationSideEffect.CaptureFormation -> {
                delay(50)

                scope.launch {
                    val uri = captureFormationAndGetUri(
                        context = context,
                        graphicsLayer = formationGraphicsLayer,
                        quarter = effect.quarter,
                    )

                    effect.onCaptureUri(effect.quarter, uri)
                }
            }

            is FormationSideEffect.ShareMultipleImages -> {
                context.shareImages(effect.imageUris)
            }
        }

        state.eventSink(FormationUiEvent.InitSideEffect)
    }
}

private suspend fun captureFormationAndGetUri(
    context: Context,
    graphicsLayer: GraphicsLayer,
    quarter: Int,
): Uri? {
    return try {
        val imageBitmap = graphicsLayer.toImageBitmap()

        val filePath = imageBitmap.saveToDisk(context, "formation_q$quarter")

        val file = File(filePath)
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: Exception) {
        Log.e("CaptureFormation", "Error capturing formation", e)
        null
    }
}
