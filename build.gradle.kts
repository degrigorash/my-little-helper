buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version Versions.kotlin apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false
    id("com.android.application") version Versions.androidPlugin apply false
    id("com.android.library") version Versions.androidPlugin apply false
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.kotlinxSerialization
}