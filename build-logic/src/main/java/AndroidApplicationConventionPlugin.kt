import com.android.build.api.dsl.ApplicationExtension
import com.wiseduck.squardbuilder.convention.ExtensionType
import com.wiseduck.squardbuilder.convention.Plugins
import com.wiseduck.squardbuilder.convention.configureBuildTypes
import com.wiseduck.squardbuilder.convention.configureKotlinAndroid
import com.wiseduck.squardbuilder.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_APPLICATION)
                apply(Plugins.KOTLIN_ANDROID)
                apply(Plugins.KSP)
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.versions.applicationId.get()
                    targetSdk = libs.versions.targetSdk.get().toInt()
                    versionCode = libs.versions.versionCode.get().toInt()
                    versionName = libs.versions.versionName.get()
                }

                buildFeatures {
                    dataBinding = true 
                }

                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION,
                )
            }
        }
    }
}
