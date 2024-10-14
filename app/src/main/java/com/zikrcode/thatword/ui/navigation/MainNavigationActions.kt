package com.zikrcode.thatword.ui.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

@Serializable
object ScreenTranslate

@Serializable
object Translate

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

    fun navigateToTranslate() {
        navController.navigate(Translate) {
            launchSingleTop = true
            restoreState = true
        }
    }
}
