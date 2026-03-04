package com.wiseduck.squadbuilder.core.common.events

import com.wiseduck.squadbuilder.core.common.R
import com.wiseduck.squadbuilder.core.common.constants.ErrorScope
import com.wiseduck.squadbuilder.core.common.extensions.userFriendlyMessage
import com.wiseduck.squadbuilder.core.common.utils.UiText

fun postErrorDialog(
    errorScope: ErrorScope,
    exception: Throwable? = null,
    confirmLabel: String = "확인",
    onConfirm: () -> Unit = {},
) {
    var title: UiText? = null
    var message: UiText = when (errorScope) {
        ErrorScope.AUTH_SESSION_EXPIRED -> UiText.StringResource(R.string.error_auth_session_expired)
        ErrorScope.GLOBAL -> UiText.StringResource(R.string.error_unknown)
    }

    exception?.let {
        message = it.userFriendlyMessage
    }

    val errorDialogSpec = DialogSpec(
        title = title,
        message = message,
        confirmButton = UiText.DynamicString(confirmLabel),
        onConfirm = onConfirm,
    )

    EventHandler.emit(event = SquadBuilderEvent.ShowDialog(dialogSpec = errorDialogSpec))
}
