package com.zikrcode.thatword.ui.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

// Route for ScreenTranslate nested graph
@Serializable object ScreenTranslateNavGraph

// Routes inside ScreenTranslate nested graph
@Serializable object ScreenTranslate
@Serializable object Customize

// Route
@Serializable object Translate

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

    fun navigateToTranslate() {
        navController.navigate(Translate) {
            popUpTo(ScreenTranslate) {
                inclusive = false // keep ScreenTranslate in back stack
            }
            launchSingleTop = true // avoid duplicate Translate screens
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
