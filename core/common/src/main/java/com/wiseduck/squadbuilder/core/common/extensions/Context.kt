package com.wiseduck.squadbuilder.core.common.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import com.wiseduck.squadbuilder.core.common.BuildConfig
import kotlin.collections.forEach

fun Context.goToPlayStore() {
    val playStoreUri = "market://details?id=${BuildConfig.PACKAGE_NAME}".toUri()
    val webUri = "https://play.google.com/store/apps/details?id=${BuildConfig.PACKAGE_NAME}".toUri()

    val playStoreIntent =
        Intent(Intent.ACTION_VIEW, playStoreUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

    try {
        startActivity(playStoreIntent)
    } catch (e: Exception) {
        val webIntent =
            Intent(Intent.ACTION_VIEW, webUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

        try {
            startActivity(webIntent)
        } catch (webException: Exception) {
            Log.e("PLAY_STORE_ERROR", "웹 브라우저도 찾을 수 없음")
        }
    }
}

fun Context.shareImages(imageUris: List<Uri>) {
    if (imageUris.isEmpty()) return

    try {
        val builder = ShareCompat
            .IntentBuilder(this)
            .setType("image/*")

        if (imageUris.size == 1) {
            builder.setStream(imageUris.first())

            val intent =
                builder.intent.apply {
                    action = android.content.Intent.ACTION_SEND
                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            startActivity(intent)
        } else {
            imageUris.forEach { builder.addStream(it) }

            val intent =
                builder.intent.apply {
                    action = android.content.Intent.ACTION_SEND_MULTIPLE
                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            startActivity(intent)
        }
    } catch (e: Exception) {
        Log.e("ShareImages", "Error sharing images", e)
    }
}
