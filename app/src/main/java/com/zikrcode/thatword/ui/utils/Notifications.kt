package com.zikrcode.thatword.ui.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.getSystemService

object Notifications {

    fun createNotificationChannel(
        id: String,
        context: Context,
        name: String
    ) {
        val notificationChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(notificationChannel)
    }

    fun createNotification(
        id: String,
        context: Context,
        @DrawableRes iconRes: Int,
        title: String
    ): Notification =
        Notification.Builder(context, id)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setShowWhen(false)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
                }
            }
            .build()
}