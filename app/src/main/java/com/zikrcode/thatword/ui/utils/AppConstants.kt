package com.zikrcode.thatword.ui.utils

import androidx.compose.ui.graphics.toArgb
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.common.theme.AppColor

object AppConstants {
    val DEFAULT_TEXT_COLOR_ARGB = AppColor.BLACK.toArgb()
    val DEFAULT_TEXT_BACKGROUND_COLOR_ARGB = AppColor.LIGHT_GRAY.toArgb()

    fun defaultInputLanguage(allLanguages: List<Language>) = allLanguages.first()
    fun defaultOutputLanguage(allLanguages: List<Language>) = allLanguages.last()
}