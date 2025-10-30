plugins {
    alias(libs.plugins.squadbuilder.android.feature)
}

android {
    namespace = "com.wiseduck.squadbuilder.feature.edit"
}

dependencies {
    implementation(project(":core:ui"))
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}