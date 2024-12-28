// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // Compose Compiler Plugin
    alias(libs.plugins.compose.compiler.plugin) apply false

    // Kotlin Serialization Plugin
    alias(libs.plugins.kotlin.serialization.plugin) apply false

    // KSP (Kotlin Symbol Processing)
    alias(libs.plugins.ksp.plugin) apply false

    // Hilt Plugin
    alias(libs.plugins.hilt.plugin) apply false
}