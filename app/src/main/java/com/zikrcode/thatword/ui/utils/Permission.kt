package com.zikrcode.thatword.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object Permission {

    fun requestDrawOverlayPermission(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        context.startActivity(intent)
    }

    fun checkDrawOverlayPermission(context: Context) = Settings.canDrawOverlays(context)
}