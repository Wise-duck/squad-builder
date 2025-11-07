import com.wiseduck.squardbuilder.convention.Plugins
import com.wiseduck.squardbuilder.convention.implementation
import com.wiseduck.squardbuilder.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFirebaseConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(Plugins.GOOGLE_SERVICES)
                apply(Plugins.FIREBASE_CRASHLYTICS)
            }

            dependencies {
                implementation(libs.firebase.bom)
                implementation(libs.firebase.analytics)
                implementation(libs.firebase.crashlytics)
            }
        }
    }
}