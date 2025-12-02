package com.wiseduck.squadbuilder.feature.webview

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.slack.circuit.codegen.annotations.CircuitInject
import com.wiseduck.squadbuilder.core.ui.SquadBuilderScaffold
import com.wiseduck.squadbuilder.feature.screens.WebViewScreen
import com.wiseduck.squadbuilder.feature.webview.component.WebViewHeader
import dagger.hilt.android.components.ActivityRetainedComponent

@CircuitInject(WebViewScreen::class, ActivityRetainedComponent::class)
@Composable
internal fun WebViewUi(
    state: WebViewUiState,
    modifier: Modifier = Modifier,
) {
    SquadBuilderScaffold(
        modifier = modifier
            .fillMaxSize(),
    ) { innerpadding ->
        WebViewHeader(
            modifier = modifier
                .padding(
                    innerpadding,
                ),
            onBackClick = { state.eventSink(WebViewUiEvent.OnBackButtonClick) },
        )
        WebViewContent(
            state = state,
            innerPadding = innerpadding,
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun WebViewContent(
    state: WebViewUiState,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )

                    webViewClient = WebViewClient()
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                    }
                    loadUrl(state.url)
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
