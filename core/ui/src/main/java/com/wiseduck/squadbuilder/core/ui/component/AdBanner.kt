package com.wiseduck.squadbuilder.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.wiseduck.squadbuilder.core.designsystem.ComponentPreview
import com.wiseduck.squadbuilder.core.designsystem.theme.SquadBuilderTheme

@SuppressLint("MissingPermission", "LocalContextResourcesRead")
@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String,
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val screenWidth = with(density) {
        context.resources.displayMetrics.widthPixels.toDp()
    }

    val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
        context,
        screenWidth.value.toInt(),
    )

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(
                    SquadBuilderTheme.radius.md,
                ),
            ),
        factory = { context ->
            AdView(context).apply {
                this.adUnitId = adUnitId
                setAdSize(adSize)

                adListener =
                    object : AdListener() {
                    }

                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            if (adView.adUnitId != adUnitId || adView.adSize != adSize) {
                adView.setAdSize(adSize)
                adView.adUnitId = adUnitId
                adView.loadAd(AdRequest.Builder().build())
            }
        },
    )
}

@ComponentPreview
@Composable
private fun AdBannerPreview() {
    SquadBuilderTheme {
        AdBanner(
            adUnitId = "ca-app-pub-3940256099942544/6300978111",
        )
    }
}
