import com.android.build.gradle.LibraryExtension
import com.wiseduck.squardbuilder.convention.libs
import com.wiseduck.squardbuilder.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }

            dependencies {
                testImplementation(libs.bundles.test.unit)
                testImplementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
