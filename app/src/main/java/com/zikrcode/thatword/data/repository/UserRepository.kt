package com.zikrcode.thatword.data.repository

import com.zikrcode.thatword.data.datasource.UserPreferencesDataSource
import com.zikrcode.thatword.domain.models.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource
) {
    companion object {
        private const val INPUT_LANGUAGE_KEY = "input_language"
        private const val OUTPUT_LANGUAGE_KEY = "output_language"
    }

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
}