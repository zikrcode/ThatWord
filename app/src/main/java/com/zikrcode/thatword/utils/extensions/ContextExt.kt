package com.zikrcode.thatword.utils.extensions

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE

@Suppress("DEPRECATION")
fun <T> Context.isServiceCurrentlyRunning(service: Class<T>): Boolean =
    (getSystemService(ACTIVITY_SERVICE) as? ActivityManager)
        ?.getRunningServices(Integer.MAX_VALUE)
        ?.find { info -> info.service.className == service.name }
        ?.foreground == true