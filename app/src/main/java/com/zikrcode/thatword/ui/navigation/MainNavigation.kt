package com.zikrcode.thatword.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zikrcode.thatword.ui.screen_translate.ScreenTranslateScreen
import com.zikrcode.thatword.ui.translate.TranslateScreen
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val startDestination = ScreenTranslate
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.let { navDestination ->
        if (navDestination.hasRoute(ScreenTranslate::class)) {
            ScreenTranslate
        } else {
            Translate
        }
    } ?: startDestination
    val navActions = remember(navController) { MainNavigationActions(navController) }
    val coroutineScope = rememberCoroutineScope()


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            MainDrawer(
                currentRoute = currentRoute,
                onNavigateToScreenTranslate = {
                    navActions.navigateToScreenTranslate()
                },
                onNavigateToTranslate = {
                    navActions.navigateToTranslate()
                },
                closeDrawer = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        },
        drawerState = drawerState
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            composable<ScreenTranslate> {
                ScreenTranslateScreen(
                    openDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
            composable<Translate> {
                TranslateScreen(
                    openDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}
