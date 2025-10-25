plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    val conventionPluginClasses = listOf(
        "android.application" to "AndroidApplicationConventionPlugin",
        "android.library" to "AndroidLibraryConventionPlugin",
        "android.application.compose" to "AndroidApplicationComposeConventionPlugin",
        "android.library.compose" to "AndroidLibraryComposeConventionPlugin",
        "android.feature" to "AndroidFeatureConventionPlugin",
        "android.hilt" to "AndroidHiltConventionPlugin",
        "jvm.library" to "JvmLibraryConventionPlugin",
        "kotlin.library.serialization" to "KotlinLibrarySerializationConventionPlugin",
        "android.retrofit" to "AndroidRetrofitConventionPlugin",
    )

    plugins {
        conventionPluginClasses.forEach { conventionPluginClass ->
            val (pluginName, className) = conventionPluginClass
            register(pluginName) {
                id = "squadbuilder.$pluginName"
                implementationClass = className
            }
        }
    }
}