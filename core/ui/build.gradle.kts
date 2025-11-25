plugins {
    alias(libs.plugins.squadbuilder.android.library)
    alias(libs.plugins.squadbuilder.android.library.compose)
}

android {
    namespace = "com.wiseduck.squadbuilder.core.ui"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(libs.play.services.ads)
}