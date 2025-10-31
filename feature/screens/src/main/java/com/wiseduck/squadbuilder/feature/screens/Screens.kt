package com.wiseduck.squadbuilder.feature.screens

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object LoginScreen : Screen

@Parcelize
data object HomeScreen : Screen

@Parcelize
data object ProfileScreen: Screen

@Parcelize
data class WebViewScreen(
    val url: String
) : Screen

@Parcelize
data class TeamDetailScreen(
    val teamId: Int,
    val teamName: String
) : Screen

@Parcelize
data class FormationScreen(
    val teamId: Int,
    val teamName: String
) : Screen

@Parcelize
data class PlayerScreen(
    val teamId: Int,
    val teamName: String
) : Screen
