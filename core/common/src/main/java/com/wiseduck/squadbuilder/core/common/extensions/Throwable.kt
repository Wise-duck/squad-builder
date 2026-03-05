package com.wiseduck.squadbuilder.core.common.extensions

import com.wiseduck.squadbuilder.core.common.R
import com.wiseduck.squadbuilder.core.common.utils.UiText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.isNetworkError(): Boolean {
    return this is UnknownHostException ||
        this is ConnectException ||
        this is SocketTimeoutException ||
        this is IOException
}

val Throwable.userFriendlyMessage: UiText
    get() = when {
        isNetworkError() -> UiText.StringResource(R.string.error_network)

        this is HttpException -> {
            val serverMessage = parseErrorMessage()
            if (serverMessage != null) {
                UiText.DynamicString(serverMessage)
            } else {
                UiText.StringResource(getHttpErrorMessageRes(code()))
            }
        }

        else -> message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.error_unknown)
    }

private fun HttpException.parseErrorMessage(): String? {
    return try {
        val errorBody = response()?.errorBody()?.string()
        if (errorBody.isNullOrBlank()) return null

        val jsonElement = Json.parseToJsonElement(errorBody)
        jsonElement.jsonObject["message"]?.jsonPrimitive?.content
    } catch (e: Exception) {
        null
    }
}

private fun getHttpErrorMessageRes(statusCode: Int): Int = when (statusCode) {
    400 -> R.string.error_http_400
    403 -> R.string.error_http_403
    404 -> R.string.error_http_404
    429 -> R.string.error_http_429
    in 400..499 -> R.string.error_http_400__499
    in 500..599 -> R.string.error_http_500__599
    else -> R.string.error_unknown
}
