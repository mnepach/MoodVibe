plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.hilt.gradle) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

subprojects {
    afterEvaluate {
        if (plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")) {
            dependencies {
                add("kapt", "com.google.dagger:hilt-compiler:2.48")
            }
        }
    }
}