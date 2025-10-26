plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Плагин Hilt Gradle должен быть применен до плагина kapt/ksp
    alias(libs.plugins.hilt.gradle)
    // В современных версиях Android Gradle Plugin (AGP) и Kotlin
    // плагин 'kotlin-kapt' должен быть применен
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.moodvibe"
    // Используем 'compileSdk = 34'
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.moodvibe"
        minSdk = 24
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    // Современный способ настройки Source/Target Compatibility и JVM Toolchain
    // Лучше всего использовать JVM Toolchain для установки компилятора
    // и оно заменяет compileOptions и kotlinOptions.jvmTarget
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // ⭐ Оптимизация: Вместо отдельного блока kotlin {}
    // В современных версиях Gradle, для Kotlin/JVM настройки
    // в Android модуле, часто лучше полагаться на настройки Android
    // или использовать Android-специфичные расширения.

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Navigation & Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    // Hilt (DI)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Glance (Widgets)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    // Shared Module
    implementation(project(":shared"))

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}