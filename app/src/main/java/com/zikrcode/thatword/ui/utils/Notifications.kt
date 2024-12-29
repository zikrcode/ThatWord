package com.zikrcode.thatword.ui.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService
import com.zikrcode.thatword.R
import com.zikrcode.thatword.utils.AppConstants

object Notifications {

    fun createNotificationChannel(context: Context) {
        val notificationChannel = NotificationChannel(
            AppConstants.SCREEN_TRANSLATE_CHANNEL_ID,
            context.getString(R.string.screen_translate_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(notificationChannel)
    }

    fun createNotification(context: Context): Notification =
        Notification.Builder(context, AppConstants.SCREEN_TRANSLATE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_screen_translate)
            .setContentTitle(context.getString(R.string.screen_translate))
            .setShowWhen(false)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
                }
            }
            .build()
}