package com.zikrcode.thatword.service

import android.content.Intent
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.zikrcode.thatword.utils.Dimens
import kotlin.math.roundToInt

abstract class OverlayService : LifecycleService(), SavedStateRegistryOwner, ViewModelStoreOwner {
    private val savedStateRegistryController by lazy { SavedStateRegistryController.create(this) }
    override val savedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private val store = ViewModelStore()
    override val viewModelStore: ViewModelStore get() = store

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: ComposeView

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        displayOverlayView()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun displayOverlayView() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
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

        overlayView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@OverlayService)
            setViewTreeViewModelStoreOwner(this@OverlayService)
            setViewTreeSavedStateRegistryOwner(this@OverlayService)
            setContent {
                var offset = remember {
                    Offset(
                        x = initialOffset.x,
                        y = initialOffset.y
                    )
                }

                OverlayView(
                    Modifier
                        .padding(Dimens.SpacingHalf)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()

                                offset = offset.copy(
                                    x = offset.x + dragAmount.x,
                                    y = offset.y + dragAmount.y
                                )

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

        windowManager.addView(overlayView, params)
    }

    @Composable
    abstract fun OverlayView(modifier: Modifier)

    abstract fun overlayViewInitialPosition(): Offset

    override fun onDestroy() {
        super.onDestroy()

        // Remove the overlay view from the window manager when the service is destroyed
        if (overlayView.isAttachedToWindow) windowManager.removeView(overlayView)
    }
}