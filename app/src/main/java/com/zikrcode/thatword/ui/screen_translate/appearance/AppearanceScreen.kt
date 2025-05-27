package com.zikrcode.thatword.ui.screen_translate.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun AppearanceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onBack,
            modifier = Modifier.padding(Dimens.SpacingDouble)
        ) {
            Text(text = "Navigate back")
        }
    }
}

@PreviewLightDark
@Composable
private fun AppearanceScreenContentPreview() {
    AppTheme {
        AppearanceScreen(
            onBack = { }
        )
    }
}