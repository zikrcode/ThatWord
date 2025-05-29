package com.zikrcode.thatword.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zikrcode.thatword.ui.navigation.MainNavigation
import com.zikrcode.thatword.ui.common.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainContent()
            }
        }
        // to have completely transparent navigation bar without any scrim color applied
        window.isNavigationBarContrastEnforced = false
    }
}

@Composable
private fun MainContent() {
    // A surface container using the 'background' color from the theme
    Surface(color = AppTheme.colorScheme.background) {
        MainNavigation()
    }
}
