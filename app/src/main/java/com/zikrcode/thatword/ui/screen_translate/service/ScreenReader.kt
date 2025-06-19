package com.zikrcode.thatword.ui.screen_translate.service

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.view.WindowManager
import androidx.core.content.getSystemService
import com.zikrcode.thatword.utils.extensions.isNotEmptyBitmap
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.graphics.createBitmap
import com.zikrcode.thatword.utils.extensions.cropIgnoringSystemBars

class ScreenReader @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val resultCode: Int,
    @Assisted private val projectionData: Intent,
    @Assisted private val onScreenReaderStop: () -> Unit
) {

    @AssistedFactory
    interface Factory {
        fun create(
            resultCode: Int,
            projectionData: Intent,
            onScreenReaderStop: () -> Unit
        ): ScreenReader
    }

    companion object {
        private const val VIRTUAL_DISPLAY = "VIRTUAL_DISPLAY"
        private const val MAX_IMAGES = 2
    }

    private var mediaProjection: MediaProjection? = null
    private val mediaProjectionCallback = object : MediaProjection.Callback() {
        override fun onStop() {
            super.onStop()
            onScreenReaderStop.invoke()
            releaseAll()
        }
    }
    private var imageReader: ImageReader? = null
    private val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    private var imageBitmap: Bitmap? = null
    private var virtualDisplay: VirtualDisplay? = null

    init {
        initMediaProjection()
        startImageCapture()
    }

    private fun initMediaProjection() {
        val mediaProjectionManager = context.getSystemService<MediaProjectionManager>()
        mediaProjection = mediaProjectionManager?.getMediaProjection(resultCode, projectionData)
        mediaProjection?.registerCallback(mediaProjectionCallback, null)
    }

    private fun startImageCapture() {
        val (width, height, density) = screenMetrics()

        imageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, MAX_IMAGES)
            .apply {
                setOnImageAvailableListener({ reader ->
                    reader.acquireLatestImage()?.let { image ->
                        val planes = image.planes
                        val buffer = planes[0].buffer
                        val pixelStride = planes[0].pixelStride
                        val rowStride = planes[0].rowStride
                        val rowPadding = rowStride - pixelStride * width

                        val bitmap = createBitmap(width + rowPadding / pixelStride, height)
                        bitmap.copyPixelsFromBuffer(buffer)
                        image.close()

                        val croppedBitmap = bitmap.cropIgnoringSystemBars(
                            width = width,
                            height = height,
                            windowMetrics = windowManager.currentWindowMetrics
                        )
                        if (croppedBitmap.isNotEmptyBitmap()) {
                            imageBitmap = croppedBitmap
                        }
                    }
                }, null)
            }

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            VIRTUAL_DISPLAY,
            width, height, density,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
            imageReader!!.surface, null, null
        )
    }

    private fun screenMetrics(): Triple<Int, Int, Int> =
        windowManager.currentWindowMetrics.bounds.run {
            Triple(
                width(),
                height(),
                context.resources.displayMetrics.densityDpi
            )
        }

    private fun releaseAll() {
        virtualDisplay?.release()
        imageReader?.surface?.release()
        imageReader?.close()
        mediaProjection?.stop()
    }

    fun capturedImageBitmap() = imageBitmap
}