// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    // ComposeCompilerPlugin
    alias(libs.plugins.compose.compiler.plugin) apply false

    // KotlinSerializationPlugin
    alias(libs.plugins.kotlin.serialization.plugin) apply false
}