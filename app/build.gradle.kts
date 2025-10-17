import Dependencies.kotlinxSerializationRetrofit
import Dependencies.okhttp3
import Dependencies.retrofit
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.grig.mylittlehelper"
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = "com.grig.mylittlehelper"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation(project(":myanimelist"))
    implementation(project(":danish"))

    implementation(Dependencies.kotlin)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.kotlinxSerialization)
    implementation(Dependencies.timber)

    implementation(kotlinxSerializationRetrofit)
    implementation(retrofit)
    implementation(okhttp3)

    implementation(Dependencies.hilt)
    kapt(Dependencies.hiltCompiler)

    implementation(Dependencies.material)
    implementation(Dependencies.composeActivity)
    implementation(Dependencies.composeViewModel)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeNavigation)
    implementation(Dependencies.composeNavigationHilt)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.datastore)

    implementation(Dependencies.coil)

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.androidxJunit)
    androidTestImplementation(Dependencies.espressoCore)
    androidTestImplementation(Dependencies.composeUiTest)

    debugImplementation(Dependencies.composeUiTooling)
    debugImplementation(Dependencies.composeUiTestManifest)
}