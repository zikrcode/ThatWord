package com.zikrcode.thatword.data.repository

import com.zikrcode.thatword.data.datasource.UserPreferencesDataSource
import com.zikrcode.thatword.domain.models.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {
    companion object {
        private const val PREFERENCES_TIMEOUT = 3000L
        private const val INPUT_LANGUAGE_KEY = "input_language"
        private const val OUTPUT_LANGUAGE_KEY = "output_language"
        private const val ICON_COLOR_KEY = "icon_color"
        private const val TEXT_COLOR_KEY = "text_color"
        private const val TEXT_BACKGROUND_COLOR_KEY = "background_color"
        private const val UPPERCASE_TEXT_KEY = "uppercase_text"
    }

    suspend fun <T> loadWithTimeoutOrNull(block: suspend CoroutineScope.() -> T) =
        withTimeoutOrNull(
            timeMillis = PREFERENCES_TIMEOUT,
            block = block
        )

    suspend fun saveInputLanguage(language: Language) {
        userPreferencesDataSource.saveStringPreference(
            key = INPUT_LANGUAGE_KEY,
            value = language.code
        )
    }

    fun readInputLanguage(): Flow<Language?> =
        userPreferencesDataSource.readStringPreference(INPUT_LANGUAGE_KEY)
            .map { tag ->
                if (tag != null) Language(tag) else null
            }

    suspend fun saveOutputLanguage(language: Language) {
        userPreferencesDataSource.saveStringPreference(
            key = OUTPUT_LANGUAGE_KEY,
            value = language.code
        )
    }

    fun readOutputLanguage(): Flow<Language?> =
        userPreferencesDataSource.readStringPreference(OUTPUT_LANGUAGE_KEY)
            .map { tag ->
                if (tag != null) Language(tag) else null
            }

    suspend fun saveIconColor(colorArgb: Int) {
        userPreferencesDataSource.saveIntPreference(
            key = ICON_COLOR_KEY,
            value = colorArgb
        )
    }

    fun readIconColor(): Flow<Int?> =
        userPreferencesDataSource.readIntPreference(ICON_COLOR_KEY)

    suspend fun saveTextColor(colorArgb: Int) {
        userPreferencesDataSource.saveIntPreference(
            key = TEXT_COLOR_KEY,
            value = colorArgb
        )
    }

    fun readTextColor(): Flow<Int?> =
        userPreferencesDataSource.readIntPreference(TEXT_COLOR_KEY)

    suspend fun saveTextBackgroundColor(colorArgb: Int) {
        userPreferencesDataSource.saveIntPreference(
            key = TEXT_BACKGROUND_COLOR_KEY,
            value = colorArgb
        )
    }

    fun readTextBackgroundColor(): Flow<Int?> =
        userPreferencesDataSource.readIntPreference(TEXT_BACKGROUND_COLOR_KEY)

    suspend fun saveUppercaseText(uppercase: Boolean) {
        userPreferencesDataSource.saveBooleanPreference(
            key = UPPERCASE_TEXT_KEY,
            value = uppercase
        )
    }

    fun readUppercaseText(): Flow<Boolean?> =
        userPreferencesDataSource.readBooleanPreference(UPPERCASE_TEXT_KEY)
}