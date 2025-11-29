package com.wiseduck.squadbuilder.feature.screens.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import com.wiseduck.squadbuilder.feature.screens.ProfileScreen
import com.wiseduck.squadbuilder.feature.screens.R

enum class SquadBuilderBottomTab(
    @DrawableRes val iconResId: Int,
    @StringRes val labelResId: Int,
    val screen: Screen,
) {
    HOME(
        iconResId = R.drawable.ic_home,
        labelResId = R.string.home_label,
        screen = HomeScreen,
    ),
    PROFILE(
        iconResId = R.drawable.ic_profile,
        labelResId = R.string.profile_label,
        screen = ProfileScreen,
    ),
}
