import com.wiseduck.squardbuilder.convention.implementation
import com.wiseduck.squardbuilder.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidRetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("squadbuilder.kotlin.library.serialization")
            }

            dependencies {
                implementation(libs.retrofit)
                implementation(libs.retrofit.kotlinx.serialization.converter)
                implementation(libs.okhttp.logging.interceptor)
            }
        }
    }
}