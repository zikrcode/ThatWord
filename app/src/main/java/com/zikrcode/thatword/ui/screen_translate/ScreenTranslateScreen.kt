package com.zikrcode.thatword.ui.screen_translate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.thatword.ui.theme.ThatWordTheme

@Composable
fun ScreenTranslateScreen(onNavigateToTranslate: () -> Unit) {
    ScreenTranslateScreenContent(onNavigateToTranslate)
}

@Preview(showBackground = true)
@Composable
fun ScreenTranslateScreenContentPreview() {
    ThatWordTheme {
        ScreenTranslateScreenContent { }
    }
}

@Composable
private fun ScreenTranslateScreenContent(onNavigateToTranslate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "ScreenTranslateScreenContent")
        Button(onClick = onNavigateToTranslate) {
            Text(text = "Translate")
        }
    }
}