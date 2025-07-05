package com.zikrcode.thatword.ui.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.zikrcode.thatword.ui.about.AboutScreen
import com.zikrcode.thatword.ui.screen_translate.ScreenTranslateScreen
import com.zikrcode.thatword.ui.screen_translate.customize.CustomizeScreen
import com.zikrcode.thatword.ui.translate.TranslateScreen
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = ScreenTranslateNavGraph
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.let { navDestination ->
        when {
            navDestination.hasRoute(ScreenTranslate::class) -> ScreenTranslate
            navDestination.hasRoute(Customize::class) -> Customize
            navDestination.hasRoute(About::class) -> About
            else -> Translate
        }
    } ?: startDestination
    val navActions = remember(navController) { MainNavigationActions(navController) }
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            MainDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                onNavigateToScreenTranslate = {
                    navActions.navigateToScreenTranslate()
                },
                onNavigateToTranslate = {
                    navActions.navigateToTranslate()
                },
                onNavigateToAbout = {
                    navActions.navigateToAbout()
                },
                closeDrawer = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        },
        drawerState = drawerState,
        gesturesEnabled = currentRoute != Customize
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            screenTranslateNavGraph(
                navActions = navActions,
                onOpenDrawer = {
                    coroutineScope.launch { drawerState.open() }
                }
            )
            composable<Translate> {
                TranslateScreen(
                    onOpenDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
            composable<About> {
                AboutScreen(
                    onOpenDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}

private fun NavGraphBuilder.screenTranslateNavGraph(
    navActions: MainNavigationActions,
    onOpenDrawer: () -> Unit
) {
    navigation<ScreenTranslateNavGraph>(startDestination = ScreenTranslate) {
        composable<ScreenTranslate> {
            ScreenTranslateScreen(
                onOpenDrawer = onOpenDrawer,
                onOpenCustomize = {
                    navActions.navigateToCustomize()
                }
            )
        }

        composable<Customize>(
            enterTransition = null,
            exitTransition = null
        ) {
            CustomizeScreen(
                onBack = {
                    navActions.navigateToScreenTranslate()
                }
            )
        }
    }
}
