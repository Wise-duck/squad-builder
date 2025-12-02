package com.wiseduck.squadbuilder.core.common.utils

import android.util.Log

/**
 * 버전 문자열 "X.Y.Z"를 정수 배열 [X, Y, Z]로 변환합니다.
 */
private fun parseVersion(versionString: String): IntArray {
    if (versionString.isBlank()) {
        Log.e("VersionParser", "Version string is blank")
        return intArrayOf()
    }

    val parts = versionString.split(".").mapNotNull { it.toIntOrNull() }

    if (parts.size < 2) {
        Log.e("VersionParser", "Invalid version format: $versionString. Expected format: X.Y.Z")
        return intArrayOf()
    }

    return parts.toIntArray()
}

/**
 * 현재 버전이 최소 버전보다 크거나 같으면 true를 반환합니다.
 */
private fun compareVersionParts(
    currentParts: IntArray,
    minParts: IntArray,
): Boolean {
    val maxLength = maxOf(currentParts.size, minParts.size)

    for (i in 0 until maxLength) {
        val currentPart = currentParts.getOrElse(i) { 0 }
        val minPart = minParts.getOrElse(i) { 0 }

        when {
            currentPart < minPart -> {
                Log.d("VersionParser", "Current part $currentPart is less than min part $minPart")
                return true
            }

            currentPart > minPart -> {
                Log.d("VersionParser", "Current part $currentPart is greater than min part $minPart")
                return false
            }
        }
    }

    return false
}

/**
 * '현재 설치된 앱 버전'이 '업데이트가 필요한 최소 버전'보다 낮으면, 업데이트가 필요하다고 판단합니다.
 */
fun isUpdateRequired(
    currentVersion: String,
    minVersion: String,
): Boolean {
    val currentParts = parseVersion(currentVersion)
    val minParts = parseVersion(minVersion)

    return compareVersionParts(currentParts, minParts)
}
