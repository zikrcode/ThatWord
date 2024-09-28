package com.zikrcode.thatword.ui.translate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
fun TranslateScreen(onNavigateToScreenTranslate: () -> Unit) {
    TranslateScreenContent(onNavigateToScreenTranslate)
}

@Preview(showBackground = true)
@Composable
fun TranslateScreenContentPreview() {
    ThatWordTheme {
        TranslateScreenContent { }
    }
}

@Composable
private fun TranslateScreenContent(onNavigateToScreenTranslate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "TranslateScreenContent")
        Button(onClick = onNavigateToScreenTranslate) {
            Text(text = "ScreenTranslate")
        }
    }
}