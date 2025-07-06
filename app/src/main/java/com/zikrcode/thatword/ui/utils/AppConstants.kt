package com.zikrcode.thatword.ui.utils

import androidx.compose.ui.graphics.toArgb
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.common.theme.AppColor

object AppConstants {
    const val PRIVACY_POLICY_URL = "https://zikrcode-privacy-policies.web.app/projects/that_word.html"
    const val LINKEDIN_URL = "https://www.linkedin.com/in/zokirjon"
    const val BUY_ME_A_COFFEE_URL = "https://buymeacoffee.com/zikrcode"
    const val MAILTO = "mailto:"
    const val EMAIL = "zikrcode@gmail.com"

    val DEFAULT_TEXT_COLOR_ARGB = AppColor.BLACK.toArgb()
    val DEFAULT_TEXT_BACKGROUND_COLOR_ARGB = AppColor.LIGHT_GRAY.toArgb()
    val DEFAULT_ICON_COLOR_ARGB = AppColor.MAIN.toArgb()

    fun defaultInputLanguage(allLanguages: List<Language>) = allLanguages.first()
    fun defaultOutputLanguage(allLanguages: List<Language>) = allLanguages.last()
}