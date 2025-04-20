package com.zikrcode.thatword.ui.common.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun AppOverlayView(
    onCloseClick: () -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .size(
                width = 64.dp,
                height = 200.dp
            ),
        shape = CircleShape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = AppTheme.colorScheme.background
        ),
        border = BorderStroke(
            width = 2.dp,
            color = AppTheme.colorScheme.main
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppCircleCardWithIcon(
                onClick = onCloseClick,
                iconPainter = painterResource(R.drawable.ic_close),
                iconContentDescription = stringResource(R.string.close),
                borderColor = AppTheme.colorScheme.red,
                containerColor = AppTheme.colorScheme.container,
                iconColor = AppTheme.colorScheme.red
            )
            Spacer(Modifier.height(Dimens.SpacingHalf))
            AppCircleCardWithIcon(
                onClick = onTranslateClick,
                iconPainter = painterResource(R.drawable.ic_screen_translate),
                iconContentDescription = stringResource(R.string.close),
                borderColor = AppTheme.colorScheme.main,
                containerColor = AppTheme.colorScheme.container,
                iconColor = AppTheme.colorScheme.main
            )
            Spacer(Modifier.height(Dimens.SpacingHalf))
            Icon(
                painter = painterResource(R.drawable.ic_move),
                tint = Color.Gray,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.SpacingSingle)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AppOverlayViewPreview() {
    AppTheme {
        AppOverlayView(
            onCloseClick = { },
            onTranslateClick = { }
        )
    }
}