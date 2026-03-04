package com.wiseduck.squadbuilder.core.common.utils

import com.wiseduck.squadbuilder.core.common.constants.ErrorScope
import com.wiseduck.squadbuilder.core.common.events.postErrorDialog
import com.wiseduck.squadbuilder.core.common.extensions.userFriendlyMessage
import retrofit2.HttpException

fun handleException(
    exception: Throwable,
    onError: (UiText) -> Unit,
    onLoginRequired: () -> Unit = {},
) {
    if (exception is HttpException && exception.code() == 401) {
        postErrorDialog(
            errorScope = ErrorScope.AUTH_SESSION_EXPIRED,
            exception = exception,
            onConfirm = onLoginRequired,
        )
        return
    }

    val message = exception.userFriendlyMessage
    onError(message)
}
