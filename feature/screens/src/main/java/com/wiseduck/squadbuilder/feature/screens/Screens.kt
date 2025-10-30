package com.wiseduck.squadbuilder.feature.screens

import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.core.model.TeamModel
import kotlinx.parcelize.Parcelize

@Parcelize
data object LoginScreen : Screen

@Parcelize
data object HomeScreen : Screen

@Parcelize
data object ProfileScreen: Screen

@Parcelize
data class TeamDetailScreen(val team: TeamModel) : Screen
