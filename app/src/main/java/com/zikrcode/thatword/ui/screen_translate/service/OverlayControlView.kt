package com.zikrcode.thatword.ui.screen_translate.service

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppVerticalSpacer
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

val OverlayControlViewWidth = 60.dp
val OverlayControlViewHeight = 100.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverlayControlView(
    onCloseClick: () -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var closeable by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .size(
                width = OverlayControlViewWidth,
                height = OverlayControlViewHeight
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = closeable,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SmallCircleButton(
                mainColor = AppTheme.colorScheme.red,
                onClick = onCloseClick,
                iconPainter = painterResource(R.drawable.ic_close),
                iconContentDescription = stringResource(R.string.close)
            )
        }
        AppVerticalSpacer(Dimens.SpacingSingle)
        Box(
            modifier = modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = AppTheme.colorScheme.main,
                    shape = CircleShape
                )
                .background(
                    color = AppTheme.colorScheme.background,
                    shape = CircleShape
                )
                .combinedClickable(
                    onClick = onTranslateClick,
                    onLongClick = { closeable = !closeable }
                ),
        ) {
            Icon(
                painter = painterResource(R.drawable.icon),
                tint = AppTheme.colorScheme.main,
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
private fun SmallCircleButton(
    mainColor: Color,
    onClick: () -> Unit,
    iconPainter: Painter,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = mainColor,
                shape = CircleShape
            )
            .background(
                color = AppTheme.colorScheme.background,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = iconContentDescription,
            tint = mainColor
        )
    }
}

@PreviewLightDark
@Composable
private fun OverlayControlViewPreview() {
    AppTheme {
        OverlayControlView(
            onCloseClick = { },
            onTranslateClick = { }
        )
    }
}