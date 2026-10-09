plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.agrichain"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.agrichain"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // =========================================================
    // Compose
    // =========================================================

    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )

    implementation(
        libs.androidx.activity.compose
    )

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(
        libs.androidx.compose.material3
    )

    implementation(
        libs.androidx.compose.material.icons.extended
    )

    // =========================================================
    // Navigation
    // =========================================================

    implementation(
        libs.androidx.navigation.compose
    )

    // =========================================================
    // QR generation
    // =========================================================

    implementation(
        "com.google.zxing:core:3.5.3"
    )

    // =========================================================
    // Google Code Scanner
    // =========================================================

    implementation(
        "com.google.android.gms:play-services-code-scanner:16.1.0"
    )

    // =========================================================
    // Google Location Services
    // =========================================================

    implementation(
        "com.google.android.gms:play-services-location:21.4.0"
    )

    // =========================================================
    // Room
    // =========================================================

    implementation(
        libs.androidx.room.runtime
    )

    ksp(
        libs.androidx.room.compiler
    )

    // =========================================================
    // Firebase
    // =========================================================

    implementation(
        platform(
            "com.google.firebase:firebase-bom:34.17.0"
        )
    )

    implementation(
        "com.google.firebase:firebase-auth"
    )

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2"
    )

    implementation(
        "com.google.firebase:firebase-firestore"
    )

    // =========================================================
    // Google Sign-In / Credential Manager
    // =========================================================

    implementation(
        "androidx.credentials:credentials:1.3.0"
    )

    implementation(
        "androidx.credentials:credentials-play-services-auth:1.3.0"
    )

    implementation(
        "com.google.android.libraries.identity.googleid:googleid:1.1.1"
    )

    // =========================================================
    // Unit tests
    // =========================================================

    testImplementation(libs.junit)

    // =========================================================
    // Debug
    // =========================================================

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )
}