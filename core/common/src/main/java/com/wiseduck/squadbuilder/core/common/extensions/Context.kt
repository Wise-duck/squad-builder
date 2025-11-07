package com.wiseduck.squadbuilder.core.common.extensions

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import com.wiseduck.squadbuilder.core.common.BuildConfig

fun Context.goToPlayStore() {
    val playStoreUri = "market://details?id=${BuildConfig.PACKAGE_NAME}".toUri()
    val webUri = "https://play.google.com/store/apps/details?id=${BuildConfig.PACKAGE_NAME}".toUri()

    val playStoreIntent = Intent(Intent.ACTION_VIEW, playStoreUri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        startActivity(playStoreIntent)
    } catch (e: Exception) {
        val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(webIntent)
        } catch (webException: Exception) {
            Log.e("PLAY_STORE_ERROR", "웹 브라우저도 찾을 수 없음")
        }
    }
}