package com.zikrcode.thatword.ui.screen_translate.service

import android.app.Service.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.zikrcode.thatword.utils.Dimens
import com.zikrcode.thatword.utils.extensions.px
import com.zikrcode.thatword.ui.utils.OverlayService
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ScreenTranslateOverlayManager(private val overlayService: OverlayService) {

    private val windowManager: WindowManager by lazy {
        overlayService.getSystemService(WINDOW_SERVICE) as WindowManager
    }
    private lateinit var overlayControlView: ComposeView
    private lateinit var overlayImageView: ComposeView

    fun displayOverlayViews(
        onCloseClick: () -> Unit,
        onTranslateClick: suspend () -> ImageBitmap?
    ) {
        displayOverlayControlView(onCloseClick, onTranslateClick)
    }

    private fun displayOverlayControlView(
        onCloseClick: () -> Unit,
        onTranslateClick: suspend () -> ImageBitmap?
    ) {
        val initialOffset = overlayViewInitialPosition()
        val params = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            x = initialOffset.x.roundToInt()
            y = initialOffset.y.roundToInt()

            gravity = Gravity.TOP or Gravity.START
            format = PixelFormat.TRANSLUCENT

            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

        overlayControlView = ComposeView(overlayService).apply {
            setViewTreeLifecycleOwner(overlayService)
            setViewTreeViewModelStoreOwner(overlayService)
            setViewTreeSavedStateRegistryOwner(overlayService)

            setContent {
                var offset = remember { initialOffset }

                OverlayControlView(
                    onCloseClick = onCloseClick,
                    onTranslateClick = {
                        overlayService.lifecycleScope.launch {
                            val imageBitmap = onTranslateClick.invoke()
                            displayOverlayImageView(imageBitmap)
                        }
                    },
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()

                                offset += dragAmount

                                params.apply {
                                    x = offset.x.roundToInt()
                                    y = offset.y.roundToInt()
                                }

                                windowManager.updateViewLayout(this@apply, params)
                            }
                        }
                )
            }
        }

        windowManager.addView(overlayControlView, params)
    }

    private fun overlayViewInitialPosition(): Offset {
        return Offset(
            x = Dimens.SpacingDouble.value.px,
            y = (overlayService.resources.displayMetrics.heightPixels - 200.dp.value.px) / 2
        )
    }

    private fun displayOverlayImageView(imageBitmap: ImageBitmap?) {
        if (imageBitmap != null) {
            val params = WindowManager.LayoutParams().apply {
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
                format = PixelFormat.TRANSLUCENT
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }

            overlayImageView = ComposeView(overlayService).apply {
                setViewTreeLifecycleOwner(overlayService)
                setViewTreeViewModelStoreOwner(overlayService)
                setViewTreeSavedStateRegistryOwner(overlayService)

                setContent {
                    Box {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Green)
                        )
                        Button(
                            onClick = {
                                println("HERE WE GO")
                                removeOverlayImageView()
//                                displayOverlayControlView()
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Text("CLOSE")
                        }
                    }
                }
            }

            windowManager.addView(overlayImageView, params)
        }
    }

    private fun removeOverlayControlView() {
        if (this::overlayControlView.isInitialized && overlayControlView.isAttachedToWindow) {
            windowManager.removeView(overlayControlView)
        }
    }

    private fun removeOverlayImageView() {
        if (this::overlayImageView.isInitialized && overlayImageView.isAttachedToWindow) {
            windowManager.removeView(overlayImageView)
        }
    }

    fun removeOverlayViews() {
        removeOverlayControlView()
        removeOverlayImageView()
    }
}