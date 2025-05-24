package com.zikrcode.thatword.utils.extensions

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap

fun Bitmap.isNotEmptyBitmap(): Boolean {
    val emptyBitmap = createBitmap(width, height, config)
    return !this.sameAs(emptyBitmap)
}