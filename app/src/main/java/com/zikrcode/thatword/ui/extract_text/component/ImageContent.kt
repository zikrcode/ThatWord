package com.zikrcode.thatword.ui.extract_text.component

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil.compose.AsyncImage
import com.zikrcode.thatword.ui.common.extension.appClipRoundedCorner
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun ImageContent(
    imageUri: Uri,
    onExtractText: () -> Unit,
    isExtracting: Boolean,
    extractedText: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .appClipRoundedCorner()
            .background(AppTheme.colorScheme.container),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .appClipRoundedCorner(),
            alpha = .4f
        )
        CircularExtractButton(
            onClick = onExtractText,
            extracting = isExtracting
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            AnimatedVisibility(extractedText != null) {
                extractedText?.let { text ->
                    ResultButton(
                        result = text,
                        modifier = Modifier.padding(bottom = Dimens.SpacingDoubleHalf)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun ImageContentPreview() {
    AppTheme {
        Box(modifier = Modifier.background(AppTheme.colorScheme.background)) {
            ImageContent(
                imageUri = Uri.EMPTY,
                onExtractText = { },
                isExtracting = false,
                extractedText = null
            )
        }
    }
}