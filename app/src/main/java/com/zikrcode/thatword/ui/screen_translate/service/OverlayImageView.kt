package com.zikrcode.thatword.ui.screen_translate.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppTheme

@Composable
fun OverlayImageView(
    imageBitmap: ImageBitmap,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.TopEnd),
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = Color.Black.copy(alpha = .2f)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.close),
                tint = AppTheme.colorScheme.red
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun OverlayImageViewPreview() {
    AppTheme {
        BoxWithConstraints {
            val bitmap = ImageBitmap(constraints.maxWidth, constraints.maxHeight)
            val canvas = Canvas(bitmap)
            val paint = Paint().apply {
                this.color = Color.LightGray
            }
            canvas.drawRect(
                left = 0f,
                top = 0f,
                right = constraints.maxWidth.toFloat(),
                bottom = constraints.maxHeight.toFloat(),
                paint = paint
            )
            OverlayImageView(
                imageBitmap = bitmap,
                onCloseClick = { }
            )
        }
    }
}