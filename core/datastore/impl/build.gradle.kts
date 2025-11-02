plugins {
    alias(libs.plugins.squadbuilder.android.library)
    alias(libs.plugins.squadbuilder.android.hilt)
    alias(libs.plugins.squadbuilder.kotlin.library.serialization)
}

android {
    namespace = "com.wiseduck.squadbuilder.core.datastore.impl"
}

dependencies {
    implementation(projects.core.datastore.api)
    implementation(projects.core.model)

    implementation(libs.androidx.datastore.preferences)
}