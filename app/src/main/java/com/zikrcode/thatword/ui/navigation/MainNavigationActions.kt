package com.zikrcode.thatword.ui.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

// Route for ScreenTranslate nested graph
@Serializable object ScreenTranslateNavGraph

// Routes inside ScreenTranslate nested graph
@Serializable object ScreenTranslate
@Serializable object Customize

// Routes
@Serializable object Translate

class MainNavigationActions(private val navController: NavHostController) {

    fun navigateToScreenTranslate() {
        navController.navigate(ScreenTranslate) {
            // avoid building up a large stack of destinations on the back stack
            popUpTo(ScreenTranslate) {
                saveState = true
            }
            // avoid multiple copies of the same destination when reselecting the same item
            launchSingleTop = true
        }
    }

    fun navigateToCustomize() {
        navController.navigate(Customize) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToTranslate() {
        navController.navigate(Translate) {
            launchSingleTop = true
            restoreState = true
        }
    }
}
