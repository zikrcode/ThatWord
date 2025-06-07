package com.zikrcode.thatword.ui.screen_translate.customize

import androidx.compose.ui.graphics.toArgb
import com.zikrcode.thatword.ui.common.theme.AppColor

data class CustomizeUiState(
    val isLoading: Boolean = false,
    val textColorArgb: Int = AppColor.MAIN.toArgb(),
    val textBackgroundColorArgb: Int = AppColor.TRANSPARENT.toArgb(),
    val uppercaseText: Boolean = false
)