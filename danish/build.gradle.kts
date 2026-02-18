plugins {
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.grig.danish"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":core"))

    implementation(libs.kotlin)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.serialization)
    implementation(libs.timber)
    implementation(libs.datastore)

    implementation(libs.kotlinx.serialization.retrofit)
    implementation(libs.retrofit)
    implementation(libs.okhttp3)
    implementation(libs.retrofit.scalars)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.material)
    implementation(libs.compose.activity)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.ui)
    implementation(libs.compose.navigation)
    implementation(libs.compose.navigation.hilt)
    implementation(libs.compose.ui.tooling.preview)

    implementation(libs.media3.exoplayer)

    implementation(libs.coil)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.ui.test)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}
