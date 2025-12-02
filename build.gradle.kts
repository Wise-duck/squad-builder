import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false

    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.google.service) apply false
}

val excludeModules: String? by project

allprojects {
    if (excludeModules?.split(",")?.contains(project.name) != true) {
        apply {
            plugin(rootProject.libs.plugins.detekt.get().pluginId)
            plugin(rootProject.libs.plugins.ktlint.get().pluginId)
        }

        afterEvaluate {
            extensions.configure<DetektExtension> {
                parallel = true
                buildUponDefaultConfig = true
                toolVersion = libs.versions.detektPlugin.get()
                config.setFrom(files("$rootDir/detekt.yml"))
            }

            extensions.configure<KtlintExtension> {
                version.set(rootProject.libs.versions.ktlintEngine.get())
                android.set(true)
                verbose.set(true)
            }
        }

        dependencies {
            detektPlugins(rootProject.libs.detekt.formatting)
        }
    }
}
