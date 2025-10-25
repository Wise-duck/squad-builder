import com.android.build.api.dsl.LibraryExtension
import com.wiseduck.squardbuilder.convention.Plugins
import com.wiseduck.squardbuilder.convention.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_LIBRARY)
                apply(Plugins.KOTLIN_COMPOSE)
            }

            extensions.configure<LibraryExtension> {
                configureCompose(this)
            }
        }
    }
}