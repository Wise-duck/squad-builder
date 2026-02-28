plugins {
    alias(libs.plugins.squadbuilder.jvm.library)
}

dependencies {
    compileOnly(
        libs.compose.stable.marker,
    )
}
