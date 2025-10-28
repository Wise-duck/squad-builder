import com.wiseduck.squardbuilder.convention.api
import com.wiseduck.squardbuilder.convention.implementation
import com.wiseduck.squardbuilder.convention.implementationProject
import com.wiseduck.squardbuilder.convention.ksp
import com.wiseduck.squardbuilder.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("squadbuilder.android.library")
                apply("squadbuilder.android.library.compose")
                apply("squadbuilder.android.hilt")
            }

            dependencies {
                implementationProject(":core:common")
                implementationProject(":core:data:api")
                implementationProject(":core:designsystem")
                implementationProject(":core:ui")
                implementationProject(":core:model")
                implementationProject(":feature:screens")

                implementation(libs.bundles.circuit)
                api(libs.circuit.codegen.annotation)
                ksp(libs.circuit.codegen.ksp)
            }
        }
    }
}