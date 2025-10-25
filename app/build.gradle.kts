plugins {
    alias(libs.plugins.squadbuilder.android.application)
    alias(libs.plugins.squadbuilder.android.application.compose)
    alias(libs.plugins.squadbuilder.android.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.wiseduck.squadbuilder"

    buildTypes {
        release {

        }
    }
}

ksp {
    arg("circuit.codegen.mode", "hilt")
    arg("enableDataBinding", "true")
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.androidx.databinding.compiler)
    ksp(libs.room.compiler)

    implementation(libs.glide)
    implementation(libs.styleable.toast)
    implementation(libs.circle.image.view)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
}