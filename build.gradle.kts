buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ben.manes.versions) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}

// Report stable updates by default (ignore alpha/beta/rc/snapshot unless current is non-stable)
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex() // simple semver check
    val isStable = stableKeyword || regex.matches(version)
    val unstableMarkers = listOf("-RC", "-M", "-beta", "-alpha", "-preview", "-eap", "-snapshot")
    val hasUnstableMarker = unstableMarkers.any { version.lowercase().contains(it.removePrefix("-")) }
    return !isStable || hasUnstableMarker
}

plugins.withId("com.github.ben-manes.versions") {
    val t = tasks.named("dependencyUpdates")
    t.configure {
        // Configure via extra properties expected by the plugin when using Kotlin DSL
        // See: https://github.com/ben-manes/gradle-versions-plugin
        (this as? com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask)?.apply {
            rejectVersionIf {
                val current = currentVersion
                val candidate = candidate.version
                isNonStable(candidate) && !isNonStable(current)
            }
            checkForGradleUpdate = true
            outputFormatter = "plain"
            outputDir = "build/dependencyUpdates"
            reportfileName = "report"
        }
    }
    // Convenience alias
    tasks.register("checkLibraryUpdates") {
        group = "help"
        description = "Checks for newer dependency versions (stable by default)"
        dependsOn(t)
    }
}