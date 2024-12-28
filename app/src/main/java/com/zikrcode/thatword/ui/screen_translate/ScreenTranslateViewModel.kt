package com.zikrcode.thatword.ui.screen_translate

import android.content.Context
import android.content.ServiceConnection
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.screen_translate.service.ScreenTranslateService
import com.zikrcode.thatword.ui.utils.MediaProjectionToken
import com.zikrcode.thatword.utils.extensions.isServiceCurrentlyRunning
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ScreenTranslateUiState(
    val isServiceRunning: Boolean = false,
    @StringRes val errorStringId: Int? = null
)

@HiltViewModel
class ScreenTranslateViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScreenTranslateUiState())
    val uiState = _uiState.asStateFlow()

    init {
        refreshServiceStatus()
    }

    private fun refreshServiceStatus() {
        val isServiceRunning = context.isServiceCurrentlyRunning(ScreenTranslateService::class.java)
        _uiState.update { state ->
            state.copy(isServiceRunning = isServiceRunning)
        }
        if (isServiceRunning) bindService()
    }

    fun startService(mediaProjectionToken: MediaProjectionToken) {
        val startIntent = ScreenTranslateService.createIntent(
            context,
            mediaProjectionToken
        )
        context.startForegroundService(startIntent)
        bindService()
        _uiState.update { state ->
            state.copy(isServiceRunning = true)
        }
    }

    fun stopService() {
        val stopIntent = ScreenTranslateService.createIntent(context)
        context.stopService(stopIntent)
        unbindService()
        _uiState.update { state ->
            state.copy(isServiceRunning = false)
        }
    }

    private lateinit var serviceConnection: ServiceConnection

    private fun bindService() {
        serviceConnection = ScreenTranslateService.bindWithService { service ->
            if (service != null) {
                service.setStopServiceCallback {
                    _uiState.update { state ->
                        state.copy(isServiceRunning = false)
                    }
                    unbindService()
                }
            } else {
                _uiState.update { state ->
                    state.copy(errorStringId = R.string.app_name)
                }
            }
        }
        context.bindService(
            ScreenTranslateService.createIntent(context),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindService() = context.unbindService(serviceConnection)

    override fun onCleared() = unbindService()
}