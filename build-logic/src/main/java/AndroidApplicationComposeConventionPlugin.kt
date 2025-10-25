import com.android.build.api.dsl.ApplicationExtension
import com.wiseduck.squardbuilder.convention.Plugins
import com.wiseduck.squardbuilder.convention.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.ANDROID_APPLICATION)
                apply(Plugins.KOTLIN_COMPOSE)
            }

            extensions.configure<ApplicationExtension> {
                configureCompose(this)
            }
        }
    }
}