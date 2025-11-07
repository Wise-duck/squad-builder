plugins {
    alias(libs.plugins.squadbuilder.android.library)
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