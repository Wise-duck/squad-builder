package com.wiseduck.squadbuilder.feature.screens.component

import com.wiseduck.squadbuilder.feature.screens.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slack.circuit.runtime.screen.Screen
import com.wiseduck.squadbuilder.feature.screens.HomeScreen
import com.wiseduck.squadbuilder.feature.screens.ProfileScreen

enum class SquadBuilderBottomTab(
    @DrawableRes val iconResId: Int,
    @DrawableRes val selectedIconResId: Int,
    @StringRes val labelResId: Int,
    val screen: Screen
) {
    HOME(
        iconResId = R.drawable.ic_home,
        selectedIconResId = R.drawable.ic_selected_home,
        labelResId = R.string.home_label,
        screen = HomeScreen,
    ),
    PROFILE(
        iconResId = R.drawable.ic_profile,
        selectedIconResId = R.drawable.ic_selected_profile,
        labelResId = R.string.profile_label,
        screen = ProfileScreen,
    )
}