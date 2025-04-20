package com.zikrcode.thatword.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}

object AppTheme {
    val colorScheme: AppColorScheme
        @Composable
        get() = if (isSystemInDarkTheme()) DarkAppColorScheme else LightAppColorScheme
}