package com.zikrcode.thatword.ui.extract_text.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.extension.appShadowElevation
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

enum class ExtractTextButtonPosition {
    LEFT,
    RIGHT
}

@Composable
fun ExtractTextButton(
    position: ExtractTextButtonPosition,
    icon: Painter,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = when (position) {
        ExtractTextButtonPosition.LEFT -> RoundedCornerShape(
            topStart = Dimens.SpacingSingleHalf,
            topEnd = Dimens.SpacingHalf,
            bottomEnd = Dimens.SpacingHalf,
            bottomStart = Dimens.SpacingSingleHalf
        )
        ExtractTextButtonPosition.RIGHT -> RoundedCornerShape(
            topStart = Dimens.SpacingHalf,
            topEnd = Dimens.SpacingSingleHalf,
            bottomEnd = Dimens.SpacingSingleHalf,
            bottomStart = Dimens.SpacingHalf
        )
    }
    Column(
        modifier = modifier
            .height(90.dp)
            .fillMaxWidth()
            .appShadowElevation(shape)
            .clip(shape)
            .border(
                width = 2.dp,
                color = AppTheme.colorScheme.main,
                shape = shape
            )
            .background(
                color = AppTheme.colorScheme.background,
                shape = shape
            )
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSingle, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = AppTheme.colorScheme.icon
        )
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.text
        )
    }
}

@PreviewLightDark
@Composable
private fun ExtractTextButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingDouble)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingHalf)
            ) {
                ExtractTextButton(
                    position = ExtractTextButtonPosition.LEFT,
                    icon = painterResource(id = R.drawable.ic_take_image),
                    label = stringResource(id = R.string.take_image),
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                ExtractTextButton(
                    position = ExtractTextButtonPosition.RIGHT,
                    icon = painterResource(id = R.drawable.ic_select_image),
                    label = stringResource(id = R.string.select_image),
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}