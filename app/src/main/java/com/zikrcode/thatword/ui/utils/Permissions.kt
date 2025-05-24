package com.zikrcode.thatword.ui.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

object Permissions {

    fun requestDrawOverlayPermission(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${context.packageName}".toUri()
        )
        context.startActivity(intent)
    }

    fun checkDrawOverlayPermission(context: Context) = Settings.canDrawOverlays(context)

    fun checkPermission(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

data class MediaProjectionToken(
    val resultCode: Int,
    val resultData: Intent
)