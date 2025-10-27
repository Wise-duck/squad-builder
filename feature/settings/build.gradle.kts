plugins {
    alias(libs.plugins.squadbuilder.android.feature)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.wiseduck.squadbuilder.feature.settings"
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}