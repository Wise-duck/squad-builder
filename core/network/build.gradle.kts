import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.squadbuilder.android.library)
    alias(libs.plugins.squadbuilder.android.retrofit)
    alias(libs.plugins.squadbuilder.android.hilt)
}

android {
    namespace = "com.wiseduck.squadbuilder.core.network"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "SERVER_BASE_URL", getServerBaseUrl("DEBUG_SERVER_BASE_URL"))
        }

        release {
            buildConfigField("String", "SERVER_BASE_URL", getServerBaseUrl("RELEASE_SERVER_BASE_URL"))
        }
    }
}

dependencies {
    implementation(projects.core.datastore.api)
}

fun getServerBaseUrl(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}
