package com.zikrcode.thatword.data.datasource

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.zikrcode.thatword.domain.models.TextWithBounds
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Data source for performing Optical Character Recognition (OCR).
 *
 * @constructor Creates an instance of [OcrDataSource].
 */
class OcrDataSource @Inject constructor() {

    /**
     * Performs Optical Character Recognition (OCR) on the provided image and extracts
     * recognized text along with its bounding boxes.
     *
     * @param imageBitmap The input image as a [Bitmap].
     *
     * @return A list of [TextWithBounds], where each item contains a recognized text
     * and the bounding rectangle, or `null` if the recognition fails.
     */
    suspend fun extractTextWithBounds(
        imageBitmap: Bitmap
    ): List<TextWithBounds>? = suspendCoroutine { continuation ->
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage = InputImage.fromBitmap(imageBitmap, 0)

        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val detectedTextWithBounds = visionText.textBlocks.mapNotNull { block ->
                    block.boundingBox?.let { box ->
                        TextWithBounds(
                            text = block.text,
                            bounds = box
                        )
                    }
                }
                continuation.resume(detectedTextWithBounds)
            }
            .addOnFailureListener {
                continuation.resume(null)
            }
    }
}