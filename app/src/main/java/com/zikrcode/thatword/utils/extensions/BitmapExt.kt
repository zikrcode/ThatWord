package com.zikrcode.thatword.utils.extensions

import android.graphics.Bitmap

fun Bitmap.isNotEmptyBitmap(): Boolean {
    val emptyBitmap = Bitmap.createBitmap(width, height, config)
    return !this.sameAs(emptyBitmap)
}