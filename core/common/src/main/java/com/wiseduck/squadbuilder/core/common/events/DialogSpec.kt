package com.wiseduck.squadbuilder.core.common.events

import com.wiseduck.squadbuilder.core.common.utils.UiText

data class SquadBuilderDialogSpec(
    val title: UiText? = null,
    val message: UiText,
    val confirmButton: UiText,
    val dismissButton: String? = null,
    val onConfirm: () -> Unit = {},
    val onDismiss: () -> Unit = {},
)
