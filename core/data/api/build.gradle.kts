plugins {
    alias(libs.plugins.squadbuilder.android.library)
}

android {
    namespace = "com.wiseduck.squadbuilder.core.data.api"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.kotlinx.coroutines.core)
}