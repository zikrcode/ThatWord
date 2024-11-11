package com.zikrcode.thatword.ui.screen_translate

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.utils.service.OverlayService
import com.zikrcode.thatword.utils.Dimens
import com.zikrcode.thatword.ui.utils.composables.AppOverlayView
import com.zikrcode.thatword.utils.AppConstants
import com.zikrcode.thatword.utils.extensions.px

class ScreenTranslateService : OverlayService() {

    companion object {
        private const val SCREEN_TRANSLATE_NOTIFICATION_ID = 100

        fun createIntent(context: Context): Intent =
            Intent(context, ScreenTranslateService::class.java)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val notification = createNotification()
        val serviceType =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        } else {
            0
        }

        startForeground(
            SCREEN_TRANSLATE_NOTIFICATION_ID,
            notification,
            serviceType
        )

        return START_STICKY
    }

    private fun createNotification(): Notification =
        Notification.Builder(this, AppConstants.SCREEN_TRANSLATE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_screen_translate)
            .setContentTitle(getString(R.string.screen_translate))
            /*
            to make notification non-dismissable by the user only in Android 13 (API level 33)
            it is non-dismissable by default in Android 12 (API level 31) and lower versions
            from Android 14 (API level 34) onwards it is dismissable and can not be avoided
             */
            .setOngoing(true)
            .setShowWhen(false)
            .apply {
                // to show notification immediately after service starts
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)
                }
            }
            .build()

    @Composable
    override fun OverlayView(modifier: Modifier) {
        AppOverlayView(modifier)
    }

    override fun overlayViewInitialPosition(): Offset {
        return Offset(
            x = Dimens.SpacingDouble.value.px,
            y = (resources.displayMetrics.heightPixels - 200.dp.value.px) / 2
        )
    }
}