package com.wiseduck.squardbuilder.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }

    when (extensionType) {
        ExtensionType.APPLICATION -> {
            extensions.configure<ApplicationExtension> {
                signingConfigs {
                    create("release") {
                        val propertiesFile = rootProject.file("keystore.properties")
                        if (propertiesFile.exists()) {
                            val properties = Properties()
                            properties.load(propertiesFile.inputStream())
                            storeFile = rootProject.file(properties["STORE_FILE"] as String)
                            storePassword = properties["STORE_PASSWORD"] as String
                            keyAlias = properties["KEY_ALIAS"] as String
                            keyPassword = properties["KEY_PASSWORD"] as String
                        }
                    }
                }

                buildTypes {
                    getByName("release") {
                        isShrinkResources = true
                        signingConfig = signingConfigs.getByName("release")
                    }
                }
            }
        }

        ExtensionType.LIBRARY -> {
            extensions.configure<LibraryExtension> {
                buildTypes {
                    getByName("release") {

                    }
                }
            }
        }
    }
}
