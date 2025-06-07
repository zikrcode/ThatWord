package com.zikrcode.thatword.ui.screen_translate.customize

sealed class CustomizeUiEvent {

    data class ChangeTextColor(val argb: Int) : CustomizeUiEvent()

    data class ChangeTextBackgroundColor(val argb: Int) : CustomizeUiEvent()

    data class ChangeUppercaseText(val uppercase: Boolean) : CustomizeUiEvent()
}