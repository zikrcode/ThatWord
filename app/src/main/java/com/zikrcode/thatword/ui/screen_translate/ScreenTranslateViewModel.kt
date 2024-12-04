package com.zikrcode.thatword.ui.screen_translate

import android.content.Context
import android.content.ServiceConnection
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.screen_translate.service.ScreenTranslateService
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

    fun toggleService() {
        val screenTranslateServiceIntent = ScreenTranslateService.createIntent(context)

        if (!_uiState.value.isServiceRunning) { // start service
            context.startForegroundService(screenTranslateServiceIntent)
            _uiState.update { state ->
                state.copy(isServiceRunning = true)
            }
            bindService()
        } else { // stop service
            if (_uiState.value.isServiceRunning) {
                context.stopService(screenTranslateServiceIntent)
                _uiState.update { state ->
                    state.copy(isServiceRunning = false)
                }
            }
            unbindService()
        }
    }

    private var serviceConnection: ServiceConnection? = null

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
            serviceConnection!!,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindService() = serviceConnection?.let { connection ->
        context.unbindService(connection)
    }

    fun refreshServiceStatus() {
        val isServiceRunning = context.isServiceCurrentlyRunning(ScreenTranslateService::class.java)
        _uiState.update { state ->
            state.copy(isServiceRunning = isServiceRunning)
        }
    }
}