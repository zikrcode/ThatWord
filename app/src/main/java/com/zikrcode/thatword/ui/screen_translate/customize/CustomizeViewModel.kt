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
            val preferences = userRepository.loadWithTimeoutOrNull {
                combine(
                    userRepository.readIconColor(),
                    userRepository.readTextColor(),
                    userRepository.readTextBackgroundColor(),
                    userRepository.readUppercaseText()
                ) { iconColorArgb, textColorArgb, backgroundColorArgb, uppercaseText ->
                    if (iconColorArgb != null && textColorArgb != null &&
                        backgroundColorArgb != null && uppercaseText != null) {
                        UserPreferences(
                            iconColorArgb = iconColorArgb,
                            textColorArgb = textColorArgb,
                            backgroundColorArgb = backgroundColorArgb,
                            uppercaseText = uppercaseText
                        )
                    } else {
                        null
                    }
                }
                    .filterNotNull()
                    .first()
            }

            if (preferences != null) {
                _uiState.update { state ->
                    state.copy(
                        iconColorArgb = preferences.iconColorArgb,
                        textColorArgb = preferences.textColorArgb,
                        textBackgroundColorArgb = preferences.backgroundColorArgb,
                        uppercaseText = preferences.uppercaseText,
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
                userRepository.readIconColor()
                    .filterNotNull()
                    .collect { argb ->
                        _uiState.update { state ->
                            state.copy(iconColorArgb = argb)
                        }
                    }
            }

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
            is CustomizeUiEvent.ChangeIconColor -> changeIconColor(event.argb)
            is CustomizeUiEvent.ChangeTextColor -> changeTextColor(event.argb)
            is CustomizeUiEvent.ChangeTextBackgroundColor -> changeTextBackgroundColor(event.argb)
            is CustomizeUiEvent.ChangeUppercaseText -> changeUppercaseText(event.uppercase)
        }
    }

    private fun changeIconColor(argb: Int) {
        viewModelScope.launch {
            userRepository.saveIconColor(argb)
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

private data class UserPreferences(
    val iconColorArgb: Int,
    val textColorArgb: Int,
    val backgroundColorArgb: Int,
    val uppercaseText: Boolean
)