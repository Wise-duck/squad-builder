plugins {
    alias(libs.plugins.squadbuilder.android.application)
    alias(libs.plugins.squadbuilder.android.application.compose)
    alias(libs.plugins.squadbuilder.android.hilt)
}

android {
    namespace = "com.wiseduck.squadbuilder"

    buildTypes {
        release {

        }
    }
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data.api)
    implementation(projects.core.data.impl)
    implementation(projects.core.datastore.api)
    implementation(projects.core.datastore.impl)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.ui)

    implementation(projects.feature.main)
    implementation(projects.feature.screens)
    implementation(projects.feature.home)
    implementation(projects.feature.settings)
    implementation(projects.feature.login)
    implementation(projects.feature.webview)
    implementation(projects.feature.detail)
    implementation(projects.feature.edit)

    implementation(libs.bundles.circuit)

    api(libs.circuit.codegen.annotation)
    ksp(libs.circuit.codegen.ksp)
}