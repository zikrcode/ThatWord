package com.zikrcode.thatword.ui.screen_translate

import androidx.annotation.StringRes
import com.zikrcode.thatword.domain.models.Language

data class ScreenTranslateUiState(
    val isLoading: Boolean = false,
    val supportedLanguages: List<Language> = emptyList(),
    val inputLanguage: Language? = null,
    val outputLanguage: Language? = null,
    val isServiceRunning: Boolean = false,
    @StringRes val errorStringId: Int? = null,
)