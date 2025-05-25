package com.zikrcode.thatword.ui.screen_translate

import android.content.Context
import android.content.ServiceConnection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.thatword.R
import com.zikrcode.thatword.data.repository.LanguageProcessingRepository
import com.zikrcode.thatword.data.repository.UserRepository
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.screen_translate.component.LanguageDirection
import com.zikrcode.thatword.ui.screen_translate.service.ScreenTranslateService
import com.zikrcode.thatword.ui.utils.MediaProjectionToken
import com.zikrcode.thatword.utils.extensions.isServiceCurrentlyRunning
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class ScreenTranslateViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languageProcessingRepository: LanguageProcessingRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    companion object {
        const val LANGUAGES_TIMEOUT = 3000L
    }

    private val _uiState = MutableStateFlow(ScreenTranslateUiState())
    val uiState = _uiState.asStateFlow()

    init {
        refreshServiceStatus()
        readLanguages()
    }

    private fun readLanguages() {
        val languages = languageProcessingRepository.allAvailableLanguages()
        _uiState.update { state ->
            state.copy(
                supportedLanguages = languages,
                isLoading = true
            )
        }

        viewModelScope.launch {
            val pair = withTimeoutOrNull(LANGUAGES_TIMEOUT) {
                combine(
                    userRepository.readInputLanguage(),
                    userRepository.readOutputLanguage()
                ) { input, output ->
                    if (input != null && output != null) input to output else null
                }
                    .filterNotNull()
                    .first() // Waits for the first non-null pair or times out
            }

            val (inputLanguage, outputLanguage) = pair
                ?: (languages.first() to languages.last()) // fallback if timeout occurs

            _uiState.update { state ->
                state.copy(
                    inputLanguage = inputLanguage,
                    outputLanguage = outputLanguage,
                    isLoading = false
                )
            }

            // Continue collecting real updates from the flows
            combine(
                userRepository.readInputLanguage(),
                userRepository.readOutputLanguage()
            ) { input, output ->
                input to output
            }.collect { (input, output) ->
                _uiState.update { state ->
                    state.copy(
                        inputLanguage = input ?: state.inputLanguage,
                        outputLanguage = output ?: state.outputLanguage
                    )
                }
            }
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
        val inputLanguage = _uiState.value.inputLanguage
        val outputLanguage = _uiState.value.outputLanguage

        if (language == inputLanguage && direction == LanguageDirection.Output ||
            language == outputLanguage && direction == LanguageDirection.Input) {
            swapLanguages()
        } else {
            viewModelScope.launch {
                when (direction) {
                    LanguageDirection.Input -> userRepository.saveInputLanguage(language)
                    LanguageDirection.Output -> userRepository.saveOutputLanguage(language)
                }
            }
        }
    }

    private fun swapLanguages() {
        val inputLanguage = _uiState.value.outputLanguage ?: return
        val outputLanguage = _uiState.value.inputLanguage ?: return

        viewModelScope.launch {
            userRepository.saveInputLanguage(inputLanguage)
            userRepository.saveOutputLanguage(outputLanguage)
        }
    }

    override fun onCleared() = unbindService()
}