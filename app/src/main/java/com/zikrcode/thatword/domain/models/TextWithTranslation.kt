package com.zikrcode.thatword.domain.models

/**
 * Represents a text along with its translation.
 *
 * @property text The original text.
 * @property translation The translated text.
 */
data class TextWithTranslation(
    val text: String,
    val translation: String
)
