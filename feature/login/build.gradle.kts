plugins {
    alias(libs.plugins.squadbuilder.android.feature)
}

android {
    namespace = "com.wiseduck.squadbuilder.feature.login"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementation(libs.kakao.auth)
}