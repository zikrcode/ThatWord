package com.zikrcode.thatword.utils.extensions

import android.content.res.Resources.getSystem

val Float.px: Float
    get() = this * getSystem().displayMetrics.density
