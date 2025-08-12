// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.gms.google-services") version "4.4.3" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

}

// build.gradle.kts (Project-level)

buildscript {
    dependencies {
        // Safe Args plugin
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.0")
    }
}
