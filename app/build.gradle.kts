import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.squadbuilder.android.application)
    alias(libs.plugins.squadbuilder.android.application.compose)
    alias(libs.plugins.squadbuilder.android.hilt)
}

android {
    namespace = "com.wiseduck.squadbuilder"

    defaultConfig {
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getApiKey("KAKAO_NATIVE_APP_KEY"))
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getApiKey("KAKAO_NATIVE_APP_KEY").trim('"')
    }

    buildFeatures {
        buildConfig = true
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
    implementation(libs.kakao.auth)

    api(libs.circuit.codegen.annotation)
    ksp(libs.circuit.codegen.ksp)
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}