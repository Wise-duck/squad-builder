plugins {
    alias(libs.plugins.squadbuilder.android.feature)
}

android {
    namespace = "com.wiseduck.squadbuilder.feature.settings"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}
