package com.zikrcode.thatword.ui.screen_translate

import android.content.Context
import android.content.ServiceConnection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.thatword.R
import com.zikrcode.thatword.data.repository.LanguageProcessingRepository
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.screen_translate.component.LanguageDirection
import com.zikrcode.thatword.ui.screen_translate.service.ScreenTranslateService
import com.zikrcode.thatword.ui.utils.MediaProjectionToken
import com.zikrcode.thatword.utils.extensions.isServiceCurrentlyRunning
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTranslateViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: LanguageProcessingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScreenTranslateUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true)
            }

            refreshServiceStatus()
            loadLanguages()

            _uiState.update { state ->
                state.copy(isLoading = false)
            }
        }
    }

    private fun loadLanguages() {
        _uiState.update { state ->
            val languages = repository.allAvailableLanguages()
            state.copy(
                supportedLanguages = languages,
                inputLanguage = languages.first(),
                outputLanguage = languages.last()
            )
        }
    }

    private fun refreshServiceStatus() {
        val isServiceRunning = context.isServiceCurrentlyRunning(ScreenTranslateService::class.java)
        _uiState.update { state ->
            state.copy(isServiceRunning = isServiceRunning)
        }
        if (isServiceRunning) bindService()
    }

    fun onEvent(event: ScreenTranslateUiEvent) {
        when (event) {
            is ScreenTranslateUiEvent.StartService -> startService(event.token)
            ScreenTranslateUiEvent.StopService -> stopService()
            is ScreenTranslateUiEvent.ChangeLanguage -> changeLanguage(event.language, event.direction)
            ScreenTranslateUiEvent.SwapLanguages -> swapLanguages()
        }
    }

    private fun startService(mediaProjectionToken: MediaProjectionToken) {
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

    private fun stopService() {
        val stopIntent = ScreenTranslateService.createIntent(context)
        context.stopService(stopIntent)
        unbindService()
        _uiState.update { state ->
            state.copy(isServiceRunning = false)
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

    private fun unbindService() {
        serviceConnection?.let { connection ->
            context.unbindService(connection)
            serviceConnection = null
        }
    }

    private fun changeLanguage(language: Language, direction: LanguageDirection) {
        _uiState.update { state ->
            when (direction) {
                LanguageDirection.Input -> state.copy(inputLanguage = language)
                LanguageDirection.Output -> state.copy(outputLanguage = language)
            }
        }
    }

    private fun swapLanguages() {
        _uiState.update { state ->
            state.copy(
                inputLanguage = state.outputLanguage,
                outputLanguage = state.inputLanguage
            )
        }
    }

    override fun onCleared() = unbindService()
}