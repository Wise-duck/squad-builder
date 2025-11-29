package com.wiseduck.squadbuilder.core.data.impl.repository

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.wiseduck.squadbuilder.core.common.utils.isUpdateRequired
import com.wiseduck.squadbuilder.core.data.api.repository.RemoteConfigRepository
import com.wiseduck.squadbuilder.core.data.impl.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) : RemoteConfigRepository {
    override suspend fun getLatestVersion(): Result<String> =
        suspendCancellableCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    val latestVersion = remoteConfig[KEY_LATEST_VERSION].asString()
                    Log.d("RemoteConfig", "Latest version: $latestVersion")
                    continuation.resume(Result.success(latestVersion))
                } else {
                    Log.e("RemoteConfig", "Fetch failed")
                    continuation.resumeWithException(it.exception ?: IllegalStateException("Fetch failed"))
                }
            }
        }

    override suspend fun shouldUpdate(): Result<Boolean> =
        suspendCancellableCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnCompleteListener {
                if (it.isSuccessful) {
                    val minimumVersion = remoteConfig[KEY_MINIMUM_VERSION].asString()
                    val currentVersion = BuildConfig.VERSION_NAME
                    Log.d("RemoteConfig", "Minimum version: $minimumVersion")
                    continuation.resume(Result.success(isUpdateRequired(currentVersion, minimumVersion)))
                } else {
                    Log.e("RemoteConfig", "Fetch failed")
                    continuation.resumeWithException(it.exception ?: IllegalStateException("Fetch failed"))
                }
            }
        }

    private companion object {
        const val KEY_LATEST_VERSION = "LatestVersion"
        const val KEY_MINIMUM_VERSION = "MinimumVersion"
    }
}
