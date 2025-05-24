package com.zikrcode.thatword.ui.screen_translate

import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.screen_translate.component.LanguageDirection
import com.zikrcode.thatword.ui.utils.MediaProjectionToken

sealed class ScreenTranslateUiEvent {

    data class StartService(val token: MediaProjectionToken) : ScreenTranslateUiEvent()

    data object StopService : ScreenTranslateUiEvent()

    data class ChangeLanguage(
        val language: Language,
        val direction: LanguageDirection
    ) : ScreenTranslateUiEvent()

    data object SwapLanguages : ScreenTranslateUiEvent()
}