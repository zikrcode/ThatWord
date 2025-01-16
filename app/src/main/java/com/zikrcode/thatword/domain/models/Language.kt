package com.zikrcode.thatword.domain.models

/**
 * Represents a language using a BCP-47 language tag.
 *
 * @property code The BCP-47 language tag (e.g., "en" for English, "es" for Spanish).
 */
data class Language(val code: String)
