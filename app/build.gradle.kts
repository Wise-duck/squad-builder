import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.squadbuilder.android.application)
    alias(libs.plugins.squadbuilder.android.application.compose)
    alias(libs.plugins.squadbuilder.android.hilt)
    alias(libs.plugins.squadbuilder.android.firebase)
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

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-3940256099942544~3347511713\"")
            buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-3940256099942544~3347511713"
        }

        getByName("release") {
            buildConfigField("String", "ADMOB_APP_ID", getApiKey("PROD_ADMOB_APP_ID"))
            buildConfigField("String", "ADMOB_BANNER_ID", getApiKey("PROD_ADMOB_BANNER_ID"))
            manifestPlaceholders["ADMOB_APP_ID"] = getApiKey("PROD_ADMOB_APP_ID")
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
    implementation(projects.feature.splash)
    implementation(projects.feature.home)
    implementation(projects.feature.settings)
    implementation(projects.feature.onboarding)
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