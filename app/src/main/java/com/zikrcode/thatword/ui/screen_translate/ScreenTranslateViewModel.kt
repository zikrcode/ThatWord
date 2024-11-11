package com.zikrcode.thatword.ui.screen_translate

import android.content.Context
import androidx.lifecycle.ViewModel
import com.zikrcode.thatword.utils.extensions.isCurrentlyRunning
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ScreenTranslateUiState(
    val isServiceRunning: Boolean = false
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

    fun startService() {
        if (!_uiState.value.isServiceRunning) {
            context.startForegroundService(ScreenTranslateService.createIntent(context))
            _uiState.update { state ->
                state.copy(isServiceRunning = true)
            }
        }
    }

    fun stopService() {
        if (_uiState.value.isServiceRunning) {
            context.stopService(ScreenTranslateService.createIntent(context))
            _uiState.update { state ->
                state.copy(isServiceRunning = false)
            }
        }
    }

    fun refreshServiceStatus() {
        _uiState.update { state ->
            state.copy(
                isServiceRunning = context.isCurrentlyRunning(ScreenTranslateService::class.java)
            )
        }
    }
}