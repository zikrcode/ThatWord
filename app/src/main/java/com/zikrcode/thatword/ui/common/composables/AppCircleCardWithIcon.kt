package com.zikrcode.thatword.ui.common.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun AppCircleCardWithIcon(
    onClick: () -> Unit,
    iconPainter: Painter,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Unspecified,
    containerColor: Color = Color.Unspecified,
    iconColor: Color = Color.Unspecified
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
        border = BorderStroke(
            width = 2.dp,
            color = borderColor
        )
    ) {
        Icon(
            painter = iconPainter,
            tint = iconColor,
            contentDescription = iconContentDescription,
            modifier = Modifier
                .size(52.dp)
                .padding(Dimens.SpacingDouble)
        )
    }
}

@PreviewLightDark
@Composable
private fun AppCircleCardWithIconPreview() {
    AppTheme {
        AppCircleCardWithIcon(
            onClick = { },
            iconPainter = painterResource(R.drawable.ic_close),
            iconContentDescription = null,
            borderColor = AppTheme.colorScheme.red,
            containerColor = AppTheme.colorScheme.container,
            iconColor = AppTheme.colorScheme.red
        )
    }
}