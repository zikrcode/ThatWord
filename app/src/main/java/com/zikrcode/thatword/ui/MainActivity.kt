package com.zikrcode.thatword.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
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
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    AppTheme {
        MainContent()
    }
}

@Composable
private fun MainContent() {
    MainNavigation()
}
