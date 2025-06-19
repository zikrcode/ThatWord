package com.zikrcode.thatword.utils.extensions

import android.graphics.Bitmap
import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.core.graphics.createBitmap

fun Bitmap.cropIgnoringSystemBars(width: Int, height: Int, windowMetrics: WindowMetrics): Bitmap {
    val insets = windowMetrics.windowInsets
        .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())

    return Bitmap.createBitmap(
        this,
        insets.left, // Crop left and set x
        insets.top,  // Crop top and set y
        width - (insets.left + insets.right), // Width excluding insets
        height - (insets.top + insets.bottom) // Height excluding insets
    )
}

fun Bitmap.isNotEmptyBitmap(): Boolean {
    val emptyBitmap = createBitmap(width, height, config)
    return !this.sameAs(emptyBitmap)
}

fun Bitmap.toMutable(): Bitmap = this.copy(this.config, true)