package com.zikrcode.thatword.ui.extract_text

import android.net.Uri

sealed class ExtractTextUiEvent {

    data class ChangeImage(val uri: Uri) : ExtractTextUiEvent()

    data object ExtractText : ExtractTextUiEvent()
}