import com.android.build.gradle.LibraryExtension
import com.wiseduck.squardbuilder.convention.ExtensionType
import com.wiseduck.squardbuilder.convention.Plugins
import com.wiseduck.squardbuilder.convention.configureBuildTypes
import com.wiseduck.squardbuilder.convention.configureKotlinAndroid
import com.wiseduck.squardbuilder.convention.detektPlugins
import com.wiseduck.squardbuilder.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import kotlin.text.toInt

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_LIBRARY)
                apply(Plugins.KOTLIN_ANDROID)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig.apply {
                    targetSdk = libs.versions.targetSdk.get().toInt()
                }

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )
            }
        }
    }
}
