package com.zikrcode.thatword.ui.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.zikrcode.thatword.R

object AppFontFamily {
    val Nunito = FontFamily(
        Font(
            resId = R.font.nunito_extra_light,
            weight = FontWeight.ExtraLight,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_extra_light_italic,
            weight = FontWeight.ExtraLight,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.nunito_light,
            weight = FontWeight.Light,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_light_italic,
            weight = FontWeight.Light,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.nunito_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.nunito_medium,
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_medium_italic,
            weight = FontWeight.Medium,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.nunito_semi_bold,
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_semi_bold_italic,
            weight = FontWeight.SemiBold,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.nunito_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_bold_italic,
            weight = FontWeight.Bold,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.nunito_extra_bold,
            weight = FontWeight.ExtraBold,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_extra_bold_italic,
            weight = FontWeight.ExtraBold,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.nunito_black,
            weight = FontWeight.Black,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.nunito_black_italic,
            weight = FontWeight.Black,
            style = FontStyle.Italic
        )
    )
}

private val defaultTypography = Typography()

val AppTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFontFamily.Nunito),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFontFamily.Nunito),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFontFamily.Nunito),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFontFamily.Nunito),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFontFamily.Nunito),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFontFamily.Nunito),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFontFamily.Nunito),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFontFamily.Nunito),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFontFamily.Nunito),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFontFamily.Nunito),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFontFamily.Nunito),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFontFamily.Nunito),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFontFamily.Nunito),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFontFamily.Nunito),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFontFamily.Nunito)
)
