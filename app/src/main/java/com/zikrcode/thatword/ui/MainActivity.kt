package com.zikrcode.thatword.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.thatword.ui.theme.ThatWordTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThatWordTheme {
                MainContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    ThatWordTheme {
        MainContent()
    }
}

@Composable
private fun MainContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        MainNavigation(modifier = Modifier.padding(innerPadding))
    }
}
