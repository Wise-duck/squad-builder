package com.wiseduck.squadbuilder.core.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.wiseduck.squadbuilder.core.common.BuildConfig

fun Context.goToPlayStore() {
    val intent =
        Intent(Intent.ACTION_VIEW, "market://details?id=${BuildConfig.PACKAGE_NAME}".toUri())
    startActivity(intent)
}