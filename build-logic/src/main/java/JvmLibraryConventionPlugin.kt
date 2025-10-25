import com.wiseduck.squardbuilder.convention.Plugins
import com.wiseduck.squardbuilder.convention.configureKotlinJvm
import com.wiseduck.squardbuilder.convention.detektPlugins
import com.wiseduck.squardbuilder.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.KOTLIN_JVM)
            }

            configureKotlinJvm()
        }
    }
}
