plugins {
    alias(libs.plugins.squadbuilder.android.library)
    id("kotlin-parcelize")
}

android {
    namespace = "com.wiseduck.squadbuilder.core.model"
}
