package com.zikrcode.thatword.domain.models

import java.util.Locale

/**
 * Represents a language using a BCP-47 language tag.
 *
 * @property code The BCP-47 language tag (e.g., "en" for English, "es" for Spanish).
 */
data class Language(val code: String) {

    /**
     * Returns the name of the language based on the BCP-47 language tag.
     *
     * @return The name of the language.
     */
    fun displayLanguage(): String = Locale
        .forLanguageTag(code)
        .displayLanguage
}
