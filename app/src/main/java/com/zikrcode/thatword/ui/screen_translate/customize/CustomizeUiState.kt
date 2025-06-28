package com.zikrcode.thatword.ui.screen_translate.customize

import com.zikrcode.thatword.ui.utils.AppConstants

data class CustomizeUiState(
    val isLoading: Boolean = false,
    val iconColorArgb: Int = AppConstants.DEFAULT_ICON_COLOR_ARGB,
    val textColorArgb: Int = AppConstants.DEFAULT_TEXT_COLOR_ARGB,
    val textBackgroundColorArgb: Int = AppConstants.DEFAULT_TEXT_BACKGROUND_COLOR_ARGB,
    val uppercaseText: Boolean = false
)