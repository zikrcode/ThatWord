package com.zikrcode.thatword.ui.extract_text

import android.net.Uri

data class ExtractTextUiState(
    val isExtracting: Boolean = false,
    val imageUri: Uri? = null,
    val extractedText: String? = null
)
