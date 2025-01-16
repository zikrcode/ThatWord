package com.zikrcode.thatword.data.repository

import android.graphics.Bitmap
import com.zikrcode.thatword.data.datasource.OcrDataSource
import com.zikrcode.thatword.domain.models.TextWithBounds
import javax.inject.Inject

/**
 * Repository for handling vision processing operations.
 *
 * This repository provides an interface for performing various vision processing tasks,
 * such as image text recognition or OCR, object detection, or other vision-related operations.
 *
 * @property ocrDataSource The data source responsible for OCR.
 *
 * @constructor Creates an instance of [VisionProcessingRepository].
 */
class VisionProcessingRepository @Inject constructor(
    private val ocrDataSource: OcrDataSource
) {

    /**
     * Recognizes text and extracts its bounding boxes from the given image.
     *
     * This method uses Optical Character Recognition (OCR) to detect text
     * in the provided [Bitmap] image and returns the recognized text along with its
     * bounding rectangles.
     *
     * @param imageBitmap The input image as a [Bitmap].
     *
     * @return A list of [TextWithBounds], or `null` if recognition fails.
     */
    suspend fun extractText(imageBitmap: Bitmap): List<TextWithBounds>? =
        ocrDataSource.extractTextWithBounds(imageBitmap)
}