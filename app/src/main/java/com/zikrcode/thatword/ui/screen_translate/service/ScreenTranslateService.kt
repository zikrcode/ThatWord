package com.zikrcode.thatword.ui.screen_translate.service

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.coroutineScope
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.utils.MediaProjectionToken
import com.zikrcode.thatword.ui.utils.Notifications
import com.zikrcode.thatword.ui.common.composables.AppOverlayView
import com.zikrcode.thatword.utils.AppConstants
import com.zikrcode.thatword.utils.Dimens
import com.zikrcode.thatword.utils.extensions.parcelable
import com.zikrcode.thatword.utils.extensions.px
import com.zikrcode.thatword.utils.service.OverlayService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScreenTranslateService : OverlayService() {

    companion object {
        private const val SCREEN_TRANSLATE_NOTIFICATION_ID = 100
        private const val EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE"
        private const val EXTRA_PROJECTION_DATA = "EXTRA_PROJECTION_DATA"

        fun createIntent(
            context: Context,
            mediaProjectionToken: MediaProjectionToken
        ): Intent = Intent(context, ScreenTranslateService::class.java).apply {
            putExtra(EXTRA_RESULT_CODE, mediaProjectionToken.resultCode)
            putExtra(EXTRA_PROJECTION_DATA, mediaProjectionToken.resultData)
        }

        fun createIntent(context: Context): Intent =
            Intent(context, ScreenTranslateService::class.java)

        fun bindWithService(
            onServiceBound: (ScreenTranslateService?) -> Unit
        ) = object : ServiceConnection {

            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                val binder = service as ScreenTranslateService.ScreenTranslateBinder
                onServiceBound(binder.getService())
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                onServiceBound(null)
            }
        }
    }

    @Inject lateinit var screenReaderFactory: ScreenReader.Factory
    private lateinit var screenReader: ScreenReader
    private var stopServiceCallback: (() -> Unit)? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        Notifications.createNotificationChannel(
            id = AppConstants.SCREEN_TRANSLATE_CHANNEL_ID,
            context = applicationContext,
            name = getString(R.string.screen_translate_channel_name)
        )
        val notification = Notifications.createNotification(
            id = AppConstants.SCREEN_TRANSLATE_CHANNEL_ID,
            context = applicationContext,
            iconRes = R.drawable.ic_screen_translate,
            title = getString(R.string.screen_translate)
        )

        startForeground(
            SCREEN_TRANSLATE_NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
        )
        startScreenReader(intent)

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return ScreenTranslateBinder()
    }

    @Composable
    override fun OverlayView(modifier: Modifier) {
        AppOverlayView(
            onCloseClick = ::stopService,
            onTranslateClick = {
                screenReader.translate { imageBitmap ->
                    imageBitmap?.let { showTranslatedImageView(it.asImageBitmap()) }
                    println("Translated ImageBitmap: ${imageBitmap?.asImageBitmap()}")
                }
            },
            modifier = modifier
        )
    }

    override fun overlayViewInitialPosition(): Offset {
        return Offset(
            x = Dimens.SpacingDouble.value.px,
            y = (resources.displayMetrics.heightPixels - 200.dp.value.px) / 2
        )
    }

    private fun startScreenReader(intent: Intent?) {
        val currentIntent = intent ?: return
        val resultCode = currentIntent.getIntExtra(EXTRA_RESULT_CODE, Activity.RESULT_CANCELED)
        val projectionData = currentIntent.parcelable<Intent>(EXTRA_PROJECTION_DATA)

        if (resultCode == Activity.RESULT_OK && projectionData != null) {
            screenReader = screenReaderFactory.create(
                coroutineScope = lifecycle.coroutineScope,
                resultCode = resultCode,
                projectionData = projectionData,
                onScreenReaderStop = ::stopService
            )
        }
    }

    private fun stopService() {
        stopServiceCallback?.invoke()
        stopSelf()
    }

    fun setStopServiceCallback(callback: () -> Unit) {
        stopServiceCallback = callback
    }

    inner class ScreenTranslateBinder : Binder() {
        fun getService(): ScreenTranslateService = this@ScreenTranslateService
    }
}