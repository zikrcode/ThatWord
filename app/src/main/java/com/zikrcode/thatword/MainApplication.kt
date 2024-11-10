package com.zikrcode.thatword

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.zikrcode.thatword.utils.AppConstants

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /*
        recreating an existing notification channel with its original values performs
        no operation, so it's safe to call this code when starting an app
        */
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            AppConstants.SCREEN_TRANSLATE_CHANNEL_ID,
            getString(R.string.screen_translate_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}