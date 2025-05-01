package com.zikrcode.thatword.ui.screen_translate.service

import android.app.Service.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.zikrcode.thatword.ui.utils.OverlayService
import com.zikrcode.thatword.utils.Dimens
import com.zikrcode.thatword.utils.extensions.px
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ScreenTranslateOverlayManager(
    private val overlayService: OverlayService,
    private val onCloseClick: () -> Unit,
    private val onTranslateClick: suspend () -> ImageBitmap?
) {
    private val windowManager: WindowManager by lazy {
        overlayService.getSystemService(WINDOW_SERVICE) as WindowManager
    }
    private var offset = controlViewInitialPosition()
    private lateinit var overlayControlView: ComposeView
    private lateinit var overlayImageView: ComposeView

    fun initOverlayViews() {
        displayOverlayControlView()
    }

    private fun displayOverlayControlView() {
        val params = controlViewWindowParam(offset)

        overlayControlView = ComposeView(overlayService).apply {
            setupViewTreeOwners()

            setContent {
                var currentOffset = remember { offset }

                OverlayControlView(
                    onCloseClick = onCloseClick,
                    onTranslateClick = {
                        overlayService.lifecycleScope.launch {
                            saveControlViewOffset(currentOffset)
                            removeOverlayControlView()
                            delay(500) // to make sure control view is not captured
                            val imageBitmap = onTranslateClick.invoke()
                            displayOverlayImageView(imageBitmap)
                        }
                    },
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()

                                currentOffset += dragAmount

                                params.apply {
                                    x = currentOffset.x.roundToInt()
                                    y = currentOffset.y.roundToInt()
                                }

                                windowManager.updateViewLayout(this@apply, params)
                            }
                        }
                )
            }
        }

        windowManager.addView(overlayControlView, params)
    }

    private fun controlViewInitialPosition(): Offset {
        val controlViewWidth = OverlayControlViewWidth.value.px
        val controlViewHeight = OverlayControlViewHeight.value.px
        val padding = Dimens.SpacingQuadruple.value.px
        return Offset(
            x = overlayService.resources.displayMetrics.widthPixels - controlViewWidth - padding,
            y = (overlayService.resources.displayMetrics.heightPixels - controlViewHeight) / 2
        )
    }

    private fun controlViewWindowParam(initialOffset: Offset) = WindowManager.LayoutParams()
        .apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = initialOffset.x.roundToInt()
            y = initialOffset.y.roundToInt()
            gravity = Gravity.TOP or Gravity.START
            format = PixelFormat.TRANSLUCENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }

    private fun View.setupViewTreeOwners() {
        setViewTreeLifecycleOwner(overlayService)
        setViewTreeViewModelStoreOwner(overlayService)
        setViewTreeSavedStateRegistryOwner(overlayService)
    }

    private fun saveControlViewOffset(newOffset: Offset) {
        offset = newOffset
    }

    private fun displayOverlayImageView(imageBitmap: ImageBitmap?) {
        if (imageBitmap != null) {
            val params = imageViewWindowParam()

            overlayImageView = ComposeView(overlayService).apply {
                setupViewTreeOwners()

                setContent {
                    OverlayImageView(
                        imageBitmap = imageBitmap,
                        onCloseClick = {
                            removeOverlayImageView()
                            displayOverlayControlView()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            windowManager.addView(overlayImageView, params)
        }
    }

    private fun imageViewWindowParam() = WindowManager.LayoutParams()
        .apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            format = PixelFormat.TRANSLUCENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
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