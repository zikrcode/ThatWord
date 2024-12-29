package com.zikrcode.thatword.ui.screen_translate.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import androidx.core.content.getSystemService
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.zikrcode.thatword.utils.extensions.isNotEmptyBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ScreenReader(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val resultCode: Int,
    private val projectionData: Intent,
    onScreenReaderStop: () -> Unit
) {
    companion object {
        private const val VIRTUAL_DISPLAY = "VIRTUAL_DISPLAY"
    }

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
        val metrics = context.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val density = metrics.densityDpi

        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            VIRTUAL_DISPLAY,
            width, height, density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader!!.surface, null, null
        )

        imageReader!!.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            if (image != null) {
                val buffer = image.planes[0].buffer
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(buffer)
                image.close()

                if (bitmap.isNotEmptyBitmap()) {
                    // TODO
                    println("Bitmap --> $bitmap")
                    imageBitmap = bitmap
                }
            }
        }, null)
    }

    fun translate(onTranslationComplete: (String) -> Unit) {
        coroutineScope.launch {
            performOpticalCharacterRecognition()?.let { detectedText ->
                translateText(detectedText)?.let { translatedText ->
                    onTranslationComplete.invoke(translatedText)
                }
            }
        }
    }

    private suspend fun performOpticalCharacterRecognition(): String? {
        return suspendCoroutine { continuation ->
            imageBitmap?.let { bitmap ->
                val inputImage = InputImage.fromBitmap(bitmap, 0)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                recognizer.process(inputImage)
                    .addOnSuccessListener { visionText ->
                        val detectedText = visionText.textBlocks.joinToString("\n") { it.text }
                        continuation.resume(detectedText)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        continuation.resume(null)
                    }
            }
        }
    }

    private suspend fun translateText(text: String): String? {
        return suspendCoroutine { continuation ->
            val translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SPANISH)
                .build()

            val translator = Translation.getClient(translatorOptions)
            translator.downloadModelIfNeeded()
                .addOnSuccessListener {
                    translator.translate(text)
                        .addOnSuccessListener { translatedText ->
                            continuation.resume(translatedText)
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                        }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    continuation.resume(null)
                }
        }
    }

    private fun releaseAll(){
        virtualDisplay?.release();
        imageReader?.surface?.release();
        imageReader?.close()
        mediaProjection?.stop()
    }
}