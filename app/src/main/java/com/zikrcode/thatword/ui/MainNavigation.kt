package com.zikrcode.thatword.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zikrcode.thatword.ui.screen_translate.ScreenTranslateScreen
import com.zikrcode.thatword.ui.translate.TranslateScreen
import kotlinx.serialization.Serializable

@Serializable
object Translate

@Serializable
object ScreenTranslate

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Any = ScreenTranslate
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        composable<ScreenTranslate> {
            ScreenTranslateScreen {
                navController.navigate(Translate)
            }
        }
        composable<Translate> {
            TranslateScreen {
                navController.navigateUp()
            }
        }
    }
}