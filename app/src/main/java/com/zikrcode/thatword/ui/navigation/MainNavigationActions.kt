package com.zikrcode.thatword.ui.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

// Route for ScreenTranslate nested graph
@Serializable object ScreenTranslateNavGraph

// Routes inside ScreenTranslate nested graph
@Serializable object ScreenTranslate
@Serializable object Customize

// Route
@Serializable object ExtractText

// Route
@Serializable object About

class MainNavigationActions(private val navController: NavHostController) {

    fun navigateToScreenTranslate() {
        navController.navigate(ScreenTranslate) {
            popUpTo(ScreenTranslate) {
                inclusive = true // clear everything including itself, then re-enter fresh
            }
            launchSingleTop = true
        }
    }

    fun navigateToCustomize() {
        navController.navigate(Customize) {
            launchSingleTop = true
        }
    }

    fun navigateToExtractText() {
        navController.navigate(ExtractText) {
            popUpTo(ScreenTranslate) {
                inclusive = false // keep ScreenTranslate in back stack
            }
            launchSingleTop = true // avoid duplicate ExtractText screens
        }
    }

    fun navigateToAbout() {
        navController.navigate(About) {
            popUpTo(ScreenTranslate) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}
