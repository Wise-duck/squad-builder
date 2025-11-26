package com.wiseduck.squadbuilder.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@SuppressLint("MissingPermission")
@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId

                adListener = object : AdListener() {

                }

                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@ComponentPreview
@Composable
private fun AdBannerPreview() {
    SquadBuilderTheme {
        AdBanner(
            adUnitId = "ca-app-pub-3940256099942544/6300978111"
        )
    }
}