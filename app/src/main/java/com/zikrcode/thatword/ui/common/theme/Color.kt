package com.zikrcode.thatword.ui.common.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object AppColor {
    val MAIN = Color(0xFF6D1CBD)
    val MAIN_LIGHT = Color(0xFF9872DD)
    val DR_WHITE = Color(0xFFFAFAFA)
    val BRIGHT_GRAY = Color(0xFFEAEAEA)
    val AMETHYST_HAZE = Color(0xFFA1A1AA)
    val DARK_GRAY = Color(0xFF44474A)
    val ONYX = Color(0xFF343A40)
    val WASHED_BLACK = Color(0xFF212529)
    val DRACULA_ORCHID = Color(0xFF2D3338)
    val WHITE = Color(0xFFFFFFFF)
    val RUSTY_RED = Color(0xFFD7263D)
    val PARADISE_PINK = Color(0xFFE94B5B)
    val BLACK = Color.Black
    val LIGHT_GRAY = Color.LightGray

    val selectableColorsArgb = setOf(
        BLACK, Color.DarkGray, Color.Gray, Color.LightGray, Color.White,
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta,
        MAIN, MAIN_LIGHT
    ).map { color ->
        color.toArgb()
    }
}

@Immutable
data class AppColorScheme(
    val background: Color,
    val container: Color,
    val divider: Color,
    val icon: Color,
    val main: Color,
    val red: Color,
    val text: Color
)

val LightAppColorScheme = AppColorScheme(
    background = AppColor.DR_WHITE,
    container = AppColor.BRIGHT_GRAY,
    divider = AppColor.AMETHYST_HAZE,
    icon = AppColor.DARK_GRAY,
    main = AppColor.MAIN,
    red = AppColor.RUSTY_RED,
    text = AppColor.DARK_GRAY
)

val DarkAppColorScheme = AppColorScheme(
    background = AppColor.WASHED_BLACK,
    container = AppColor.ONYX,
    divider = AppColor.DRACULA_ORCHID,
    icon = AppColor.WHITE,
    main = AppColor.MAIN_LIGHT,
    red = AppColor.PARADISE_PINK,
    text = AppColor.WHITE
)