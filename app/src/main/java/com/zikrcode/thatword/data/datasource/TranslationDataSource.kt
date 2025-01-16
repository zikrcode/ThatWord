package com.zikrcode.thatword.data.datasource

import android.util.LruCache
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.domain.models.TextWithTranslation
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Data source for performing text translation.
 *
 * @constructor Creates an instance of [TranslationDataSource].
 */
class TranslationDataSource @Inject constructor() {

    companion object {
        /**
         * Maximum number of translator instances to retain in the LRU cache.
         * Each instance is configured based on source and target languages.
         */
        private const val MAX_TRANSLATOR_CACHE_SIZE = 3
    }

    // LRU Cache for managing Translator instances
    private val translators =
        object : LruCache<TranslatorOptions, Translator>(MAX_TRANSLATOR_CACHE_SIZE) {

            override fun create(options: TranslatorOptions): Translator {
                return Translation.getClient(options)
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: TranslatorOptions,
                oldValue: Translator,
                newValue: Translator?,
            ) {
                // Release resources when a Translator is removed
                oldValue.close()
            }
        }

    /**
     * Retrieves a list of all available languages supported by the translation library.
     *
     * @return A list of [Language] objects, each representing a supported language.
     */
    fun allAvailableLanguages(): List<Language> =
        TranslateLanguage.getAllLanguages().map { code ->
            Language(code)
        }

    /**
     * Translates a text from the source language to the target language.
     * This method downloads the required translator model if it is not already available.
     * The translator is cached to optimize performance for subsequent translations.
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
    ): TextWithTranslation? {
        val sourceLanguageCode = TranslateLanguage.fromLanguageTag(sourceLanguage.code)
        val targetLanguageCode = TranslateLanguage.fromLanguageTag(targetLanguage.code)

        if (sourceLanguageCode == null || targetLanguageCode == null) {
            // source/target language is not supported
            return null
        }

        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode)
            .build()

        val translator = translators[translatorOptions]
        val translatorModelDownloaded = downloadTranslatorModelIfNeeded(translator)

        if (!translatorModelDownloaded) {
            // model could not be downloaded
            return null
        }

        return suspendCoroutine { continuation ->
            translator.translate(text)
                .addOnSuccessListener { translatedText ->
                    val textWithTranslation = TextWithTranslation(
                        text = text,
                        translation = translatedText
                    )
                    continuation.resume(textWithTranslation)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    /**
     * Downloads the translation model for the specified translator if required.
     *
     * @param translator The translator instance for which the model should be downloaded.
     *
     * @return `true` if the model was successfully downloaded or already available,
     * otherwise `false`.
     */
    private suspend fun downloadTranslatorModelIfNeeded(translator: Translator): Boolean =
        suspendCoroutine { continuation ->
            translator.downloadModelIfNeeded()
                .addOnCompleteListener { task ->
                    continuation.resume(task.isSuccessful)
                }
        }
}