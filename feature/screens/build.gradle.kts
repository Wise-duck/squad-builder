plugins {
    alias(libs.plugins.squadbuilder.android.library)
    alias(libs.plugins.squadbuilder.android.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.wiseduck.squadbuilder.feature.screens"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)

    implementation(libs.circuit.foundation)
    implementation(project(":core:model"))
}