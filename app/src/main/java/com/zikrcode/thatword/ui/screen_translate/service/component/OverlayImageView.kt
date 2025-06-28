package com.zikrcode.thatword.ui.screen_translate.service.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppColor
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

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
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Dimens.SpacingSingle),
            colors = IconButtonDefaults.iconButtonColors().copy(
                containerColor = AppTheme.colorScheme.red.copy(alpha = .8f)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.close),
                tint = AppColor.DR_WHITE
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
                this.color = AppTheme.colorScheme.background
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