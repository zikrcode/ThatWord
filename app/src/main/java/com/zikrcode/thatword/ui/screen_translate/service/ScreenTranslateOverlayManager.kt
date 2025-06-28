package com.zikrcode.thatword.ui.screen_translate.service

import android.app.Service.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.zikrcode.thatword.ui.screen_translate.service.component.OverlayControlView
import com.zikrcode.thatword.ui.screen_translate.service.component.OverlayControlViewWidth
import com.zikrcode.thatword.ui.screen_translate.service.component.OverlayImageView
import com.zikrcode.thatword.ui.utils.OverlayService
import com.zikrcode.thatword.utils.Dimens
import com.zikrcode.thatword.utils.extensions.px
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ScreenTranslateOverlayManager(
    private val overlayService: OverlayService,
    private val onCloseClick: () -> Unit,
    private val onTranslateClick: suspend () -> ImageBitmap?
) {
    companion object {
        private const val ANIMATION_MAX_DURATION = 100L
    }
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
                var isTranslating by rememberSaveable {
                    mutableStateOf(false)
                }

                OverlayControlView(
                    translating = isTranslating,
                    onCloseClick = onCloseClick,
                    onTranslateClick = {
                        isTranslating = true
                        saveControlViewOffset(currentOffset)
                        overlayService.lifecycleScope.launch {
                            val imageBitmap = onTranslateClick.invoke()
                            removeOverlayControlView {
                                displayOverlayImageView(imageBitmap)
                                isTranslating = false
                            }
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
        val padding = Dimens.SpacingQuadruple.value.px
        return Offset(
            x = overlayService.resources.displayMetrics.widthPixels - controlViewWidth - padding,
            y = 0f
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
                            removeOverlayImageView {
                                displayOverlayControlView()
                            }
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

    private fun removeOverlayControlView(onPostDelay: (() -> Unit)? = null) {
        if (this::overlayControlView.isInitialized && overlayControlView.isAttachedToWindow) {
            overlayControlView.postDelayed({
                windowManager.removeView(overlayControlView)
                onPostDelay?.invoke()
            }, ANIMATION_MAX_DURATION)
        }
    }

    private fun removeOverlayImageView(onPostDelay: (() -> Unit)? = null) {
        if (this::overlayImageView.isInitialized && overlayImageView.isAttachedToWindow) {
            overlayImageView.postDelayed({
                windowManager.removeView(overlayImageView)
                onPostDelay?.invoke()
            }, ANIMATION_MAX_DURATION)
        }
    }

    fun removeOverlayViews() {
        removeOverlayControlView()
        removeOverlayImageView()
    }
}