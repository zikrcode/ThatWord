package com.zikrcode.thatword.data.repository

import com.zikrcode.thatword.data.datasource.TranslationDataSource
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.domain.models.TextWithTranslation
import javax.inject.Inject

/**
 * Repository for handling language-related processing tasks.
 *
 * This repository provides an interface for performing language-related operations,
 * such as retrieving available languages and translating text.
 *
 * @property translationDataSource The data source responsible for handling text translation tasks.
 *
 * @constructor Creates an instance of [LanguageProcessingRepository].
 */
class LanguageProcessingRepository @Inject constructor(
    private val translationDataSource: TranslationDataSource
) {

    /**
     * Retrieves all languages supported by the translation library.
     *
     * @return A list of [Language] objects representing supported languages.
     */
    fun allAvailableLanguages(): List<Language> = translationDataSource.allAvailableLanguages()

    /**
     * Translates a text from the source language to the target language.
     *
     * @param text The text to translate.
     * @param sourceLanguage The source language as a [Language] object.
     * @param targetLanguage The target language as a [Language] object.
     *
     * @return A [TextWithTranslation] object containing the original text and its translation,
     * or `null` if the translation failed because of either the source/target language is not
     * supported or the model could not be downloaded.
     */
    suspend fun translateText(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): TextWithTranslation? =
        translationDataSource.translateText(text, sourceLanguage, targetLanguage)
}