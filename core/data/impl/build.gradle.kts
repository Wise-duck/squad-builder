plugins {
    alias(libs.plugins.squadbuilder.android.library)
    alias(libs.plugins.squadbuilder.android.hilt)
    alias(libs.plugins.squadbuilder.kotlin.library.serialization)
}

android {
    namespace = "com.wiseduck.squadbuilder.core.data.impl"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"${libs.versions.versionName.get()}\"")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data.api)
    implementation(projects.core.datastore.api)
    implementation(projects.core.model)
    implementation(projects.core.network)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)
}
