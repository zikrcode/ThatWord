package com.zikrcode.thatword.ui.screen_translate.customize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.thatword.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomizeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomizeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        readPreferences()
        observePreferenceUpdates()
    }

    private fun readPreferences() {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }

        viewModelScope.launch {
            val triple = userRepository.loadWithTimeoutOrNull {
                combine(
                    userRepository.readTextColor(),
                    userRepository.readTextBackgroundColor(),
                    userRepository.readUppercaseText()
                ) { textColor, backgroundColor, uppercaseText ->
                    if (textColor != null && backgroundColor != null && uppercaseText != null) {
                        Triple(textColor, backgroundColor, uppercaseText)
                    } else {
                        null
                    }
                }
                    .filterNotNull()
                    .first()
            }

            if (triple != null) {
                val (textColor, backgroundColor, uppercaseText) = triple
                _uiState.update { state ->
                    state.copy(
                        textColorArgb = textColor,
                        textBackgroundColorArgb = backgroundColor,
                        uppercaseText = uppercaseText,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { state -> state.copy(isLoading = false) }
            }
        }
    }

    private fun observePreferenceUpdates() {
        viewModelScope.apply {
            launch {
                userRepository.readTextColor()
                    .filterNotNull()
                    .collect { argb ->
                        _uiState.update { state ->
                            state.copy(textColorArgb = argb)
                        }
                    }
            }

            launch {
                userRepository.readTextBackgroundColor()
                    .filterNotNull()
                    .collect { argb ->
                        _uiState.update { state ->
                            state.copy(textBackgroundColorArgb = argb)
                        }
                    }
            }

            launch {
                userRepository.readUppercaseText()
                    .filterNotNull()
                    .collect { uppercase ->
                        _uiState.update { state ->
                            state.copy(uppercaseText = uppercase)
                        }
                    }
            }
        }
    }

    fun onEvent(event: CustomizeUiEvent) {
        when (event) {
            is CustomizeUiEvent.ChangeTextColor -> changeTextColor(event.argb)
            is CustomizeUiEvent.ChangeTextBackgroundColor -> changeTextBackgroundColor(event.argb)
            is CustomizeUiEvent.ChangeUppercaseText -> changeUppercaseText(event.uppercase)
        }
    }

    private fun changeTextColor(argb: Int) {
        viewModelScope.launch {
            userRepository.saveTextColor(argb)
        }
    }

    private fun changeTextBackgroundColor(argb: Int) {
        viewModelScope.launch {
            userRepository.saveTextBackgroundColor(argb)
        }
    }

    private fun changeUppercaseText(uppercase: Boolean) {
        viewModelScope.launch {
            userRepository.saveUppercaseText(uppercase)
        }
    }
}