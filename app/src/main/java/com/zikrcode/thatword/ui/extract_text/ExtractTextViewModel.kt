package com.zikrcode.thatword.ui.extract_text

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zikrcode.thatword.data.repository.VisionProcessingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtractTextViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val visionProcessingRepository: VisionProcessingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExtractTextUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ExtractTextUiEvent) {
        when (event) {
            is ExtractTextUiEvent.ChangeImage -> changeImage(event.uri)
            ExtractTextUiEvent.ExtractText -> extractText()
        }
    }

    private fun changeImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(imageUri = uri)
            }
        }
    }

    private fun extractText() {
        val imageUri = _uiState.value.imageUri ?: return

        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isExtracting = true,
                    extractedText = null
                )
            }

            val decodedBitmap = context.contentResolver.openInputStream(imageUri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }

            decodedBitmap?.let { bitmap ->
                val extractedText = visionProcessingRepository.extractText(bitmap)
                    ?.joinToString(separator = " ") { textWithBounds ->
                        textWithBounds.text
                    }
                _uiState.update { state ->
                    state.copy(extractedText = extractedText)
                }
            }

            _uiState.update { state ->
                state.copy(isExtracting = false)
            }
        }
    }
}