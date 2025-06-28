package com.zikrcode.thatword.ui.screen_translate.service

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.zikrcode.thatword.data.repository.LanguageProcessingRepository
import com.zikrcode.thatword.data.repository.UserRepository
import com.zikrcode.thatword.data.repository.VisionProcessingRepository
import com.zikrcode.thatword.domain.models.TextWithBounds
import com.zikrcode.thatword.domain.models.TextWithTranslation
import com.zikrcode.thatword.ui.utils.AppConstants
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import androidx.core.graphics.withTranslation
import com.zikrcode.thatword.utils.extensions.toTransparent

class ImageTranslator @Inject constructor(
    private val languageProcessingRepository: LanguageProcessingRepository,
    private val userRepository: UserRepository,
    private val visionProcessingRepository: VisionProcessingRepository
) {

    suspend fun translate(bitmap: Bitmap): ImageBitmap? {
        val extractedText = extractText(bitmap)
        if (extractedText.isNullOrEmpty()) {
            println("Extract text failed")
            return null
        }

        val translationsMap = translationsMap(extractedText)
        if (translationsMap.isEmpty()) {
            println("Translations map is empty")
            return null
        }

        return createTranslatedImage(
            bitmap = bitmap,
            translationsMap = translationsMap
        )
    }

    private suspend fun extractText(bitmap: Bitmap): List<TextWithBounds>? =
        visionProcessingRepository.extractText(bitmap)

    private suspend fun translationsMap(
        extractedTexts: List<TextWithBounds>
    ): Map<TextWithBounds, TextWithTranslation> {
        val allLanguages = languageProcessingRepository.allAvailableLanguages()
        val inputLanguage = userRepository.readInputLanguage()
            .first() ?: AppConstants.defaultInputLanguage(allLanguages)
        val outputLanguage = userRepository.readOutputLanguage()
            .first() ?: AppConstants.defaultOutputLanguage(allLanguages)

        val translation = extractedTexts.mapNotNull { textBlock ->
            languageProcessingRepository.translateText(
                text = textBlock.text,
                sourceLanguage = inputLanguage,
                targetLanguage = outputLanguage
            )?.let { translation ->
                textBlock to translation
            }
        }.toMap()

        return translation
    }

    private suspend fun createTranslatedImage(
        bitmap: Bitmap,
        translationsMap: Map<TextWithBounds, TextWithTranslation>
    ): ImageBitmap {
        val transparentBitmap = bitmap.toTransparent()
        val canvas = Canvas(transparentBitmap)
        val textPaint = createBaseTextPaint()
        val backgroundPaint = createBackgroundPaint()

        translationsMap.forEach { (bounds, translation) ->
            drawTranslationOnCanvas(
                canvas = canvas,
                textWithBounds = bounds,
                textWithTranslation = translation,
                textPaint = textPaint,
                backgroundPaint = backgroundPaint
            )
        }
        return transparentBitmap.asImageBitmap()
    }

    private suspend fun createBaseTextPaint(): TextPaint {
        val textColorArgb = userRepository.readTextColor().first()
            ?: AppConstants.DEFAULT_TEXT_COLOR_ARGB

        return TextPaint().apply {
            color = textColorArgb
            isAntiAlias = true
        }
    }

    private suspend fun createBackgroundPaint(): Paint {
        val textBackgroundColorArgb = userRepository.readTextBackgroundColor().first()
            ?: AppConstants.DEFAULT_TEXT_BACKGROUND_COLOR_ARGB

        return Paint().apply {
            color = textBackgroundColorArgb
            style = Paint.Style.FILL
        }
    }

    private suspend fun drawTranslationOnCanvas(
        canvas: Canvas,
        textWithBounds: TextWithBounds,
        textWithTranslation: TextWithTranslation,
        textPaint: TextPaint,
        backgroundPaint: Paint
    ) {
        val rect = textWithBounds.bounds

        // Draw background
        canvas.drawRect(rect, backgroundPaint)

        // Prepare translated text with case preference
        val translatedText = formatTextCase(textWithTranslation.translation)

        // Calculate optimal text size and layout
        val layout = createAutoSizedLayout(
            text = translatedText,
            paint = textPaint,
            rect = rect
        )

        // Center vertically in the given rect
        val x = rect.left.toFloat()
        val y = rect.top + (rect.height() - layout.height) / 2f

        canvas.withTranslation(x, y) {
            layout.draw(this)
        }
    }

    private suspend fun formatTextCase(text: String) =
        if (userRepository.readUppercaseText().first() == true) text.uppercase() else text

    private fun createAutoSizedLayout(
        text: String,
        paint: TextPaint,
        rect: Rect,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
    ): StaticLayout {
        val bestSize = findOptimalTextSize(text, paint, rect)
        paint.textSize = bestSize

        return StaticLayout.Builder
            .obtain(text, 0, text.length, paint, rect.width())
            .setAlignment(alignment)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()
    }

    private fun findOptimalTextSize(
        text: String,
        paint: TextPaint,
        rect: Rect,
        minSize: Float = 2f,
        maxSize: Float = 200f,
        threshold: Float = 0.5f
    ): Float {
        var low = minSize
        var high = maxSize
        var bestFit = low

        while (high - low > threshold) {
            val mid = (low + high) / 2
            paint.textSize = mid

            val layout = StaticLayout.Builder
                .obtain(text, 0, text.length, paint, rect.width())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(0f, 1f)
                .setIncludePad(false)
                .build()

            if (layout.height <= rect.height()) {
                bestFit = mid
                low = mid + threshold
            } else {
                high = mid - threshold
            }
        }

        return bestFit
    }
}
