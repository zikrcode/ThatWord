package com.zikrcode.thatword.ui.screen_translate.service

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Paint
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.view.WindowInsets
import android.view.WindowManager
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.getSystemService
import com.zikrcode.thatword.data.repository.LanguageProcessingRepository
import com.zikrcode.thatword.data.repository.VisionProcessingRepository
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.domain.models.TextWithBounds
import com.zikrcode.thatword.domain.models.TextWithTranslation
import com.zikrcode.thatword.utils.extensions.isNotEmptyBitmap
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import androidx.core.graphics.createBitmap

class ScreenReader @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    private val languageProcessingRepository: LanguageProcessingRepository,
    private val visionProcessingRepository: VisionProcessingRepository,
    @Assisted private val resultCode: Int,
    @Assisted private val projectionData: Intent,
    @Assisted private val onScreenReaderStop: () -> Unit
) {

    @AssistedFactory
    interface Factory {
        fun create(
            resultCode: Int,
            projectionData: Intent,
            onScreenReaderStop: () -> Unit
        ): ScreenReader
    }

    companion object {
        private const val VIRTUAL_DISPLAY = "VIRTUAL_DISPLAY"
    }

    private var windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null
    private var imageBitmap: Bitmap? = null
    private val mediaProjectionCallback = object : MediaProjection.Callback() {
        override fun onStop() {
            super.onStop()
            onScreenReaderStop.invoke()
            releaseAll()
        }
    }

    init {
        initMediaProjection()
        startImageCapture()
    }

    private fun initMediaProjection() {
        val mediaProjectionManager = context.getSystemService<MediaProjectionManager>()
        mediaProjection = mediaProjectionManager?.getMediaProjection(resultCode, projectionData)
        mediaProjection?.registerCallback(mediaProjectionCallback, null)
    }

    private fun startImageCapture() {
        val (width, height, density) = screenMetrics()

        imageReader = ImageReader.newInstance(
            width, height,
            PixelFormat.RGBA_8888,
            2
        )
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            VIRTUAL_DISPLAY,
            width, height, density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
            imageReader!!.surface, null, null
        )

        imageReader!!.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            if (image != null) {
                val buffer = image.planes[0].buffer
                val bitmap = createBitmap(width, height)
                bitmap.copyPixelsFromBuffer(buffer)
                image.close()

                val croppedBitmap = createCroppedBitmap(bitmap, width, height)
                if (croppedBitmap.isNotEmptyBitmap()) {
                    imageBitmap = croppedBitmap
                }
            }
        }, null)
    }

    private fun screenMetrics(): Triple<Int, Int, Int> {
        val bounds = windowManager.currentWindowMetrics.bounds

        return Triple(
            bounds.width(),
            bounds.height(),
            context.resources.displayMetrics.densityDpi
        )
    }

    private fun createCroppedBitmap(
        bitmap: Bitmap,
        width: Int,
        height: Int
    ): Bitmap {
        val insets = systemBarInsets()
        val captureWidth = width - (insets.left + insets.right)
        val captureHeight = height - (insets.top + insets.bottom)

        return Bitmap.createBitmap(
            bitmap,
            insets.left, // Crop left
            insets.top,  // Crop top
            captureWidth, // Width excluding insets
            captureHeight // Height excluding insets
        )
    }

    private fun systemBarInsets(): Insets {
        val metrics = windowManager.currentWindowMetrics
        return metrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
    }

    suspend fun translate(): ImageBitmap? {
        imageBitmap?.let { originalBitmap ->
            val mutableBitmap = createMutableBitmap(originalBitmap)
            val canvas = Canvas(mutableBitmap)
            val paint = createTextPaint()
            val backgroundPaint = createBackgroundPaint()

            val extractedTextWithBounds = extractTextWithBounds(originalBitmap)

            return if (extractedTextWithBounds != null) {
                applyTranslationsToCanvas(
                    canvas,
                    extractedTextWithBounds,
                    paint,
                    backgroundPaint
                )
                mutableBitmap.asImageBitmap()
            } else {
                println("OCR failed")
                null
            }
        } ?: return null
    }

    private fun createMutableBitmap(bitmap: Bitmap): Bitmap {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun createTextPaint(): Paint {
        return Paint().apply {
            color = Color.BLACK
            textSize = 40f
            style = Paint.Style.FILL
        }
    }

    private fun createBackgroundPaint(): Paint {
        return Paint().apply {
            color = Color.YELLOW
            alpha = 200
        }
    }

    private suspend fun extractTextWithBounds(bitmap: Bitmap): List<TextWithBounds>? {
        return visionProcessingRepository.extractText(bitmap)
    }

    private suspend fun applyTranslationsToCanvas(
        canvas: Canvas,
        detectedTexts: List<TextWithBounds>,
        textPaint: Paint,
        backgroundPaint: Paint
    ) {
        for (textWithBounds in detectedTexts) {
            val translation = translateText(
                textWithBounds.text,
                sourceLanguage = Language("en"),
                targetLanguage = Language("ru")
            )
            if (translation != null) {
                drawTranslationOnCanvas(canvas, textWithBounds, translation, textPaint, backgroundPaint)
            }
        }
    }

    private suspend fun translateText(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): TextWithTranslation? {
        return languageProcessingRepository.translateText(text, sourceLanguage, targetLanguage)
    }

    private fun drawTranslationOnCanvas(
        canvas: Canvas,
        textWithBounds: TextWithBounds,
        translation: TextWithTranslation,
        textPaint: Paint,
        backgroundPaint: Paint
    ) {
        val rect = textWithBounds.bounds

        // Draw the background rectangle
        canvas.drawRect(rect, backgroundPaint)

        // Draw the translated text
        canvas.drawText(
            translation.translation,
            rect.left.toFloat(),
            rect.bottom.toFloat(),
            textPaint
        )
    }

    private fun releaseAll() {
        virtualDisplay?.release()
        imageReader?.surface?.release()
        imageReader?.close()
        mediaProjection?.stop()
    }
}