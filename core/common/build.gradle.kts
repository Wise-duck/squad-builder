plugins {
    alias(libs.plugins.squadbuilder.android.library)
    alias(libs.plugins.squadbuilder.android.hilt)
}

android {
    namespace = "com.wiseduck.squadbuilder.core.common"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "PACKAGE_NAME", "\"${libs.versions.applicationId.get()}\"")
    }
}
