package com.wiseduck.squadbuilder.core.common.extensions

import android.util.Log
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.Instant

object DateFormats {
    const val YYYY_MM_DD_KOREAN = "yyyy년 MM월 dd일"
    const val YY_MM_DD_DOT = "yy.MM.dd"
    const val YY_MM_DD_DASH = "yyyy-MM-dd"
    const val A_HH_MM_KOREAN = "a hh:mm"
    const val YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"
}

fun String.toFormattedDate(format: String): String {
    return try {
        val instant = Instant.parse(this)
        val zonedDateTime = instant.atZone(ZoneId.of("Asia/Seoul"))
        val formatter = DateTimeFormatter.ofPattern(format)
        zonedDateTime.format(formatter)
    } catch (e: DateTimeParseException) {
        Log.e("DateParseError", "Failed to parse date: $this", e)
        this
    }
}