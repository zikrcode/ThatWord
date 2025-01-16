package com.zikrcode.thatword.domain.models

import android.graphics.Rect

/**
 * Represents a recognized text along with its bounding box.
 *
 * @property text The recognized text content.
 * @property bounds The bounding rectangle of the recognized text.
 */
data class TextWithBounds(
    val text: String,
    val bounds: Rect
)
