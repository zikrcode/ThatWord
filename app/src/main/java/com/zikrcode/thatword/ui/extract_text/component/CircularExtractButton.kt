package com.zikrcode.thatword.ui.extract_text.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppDotsPulsingLoading
import com.zikrcode.thatword.ui.common.composables.AppVerticalSpacer
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.ui.screen_translate.component.CircularPowerButtonSize
import com.zikrcode.thatword.utils.Dimens

@Composable
fun CircularExtractButton(
    onClick: () -> Unit,
    extracting: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(CircularPowerButtonSize)
            .shadow(
                elevation = Dimens.ElevationSingleHalf,
                shape = CircleShape
            )
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
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_extract_text),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = AppTheme.colorScheme.icon
        )
        AppVerticalSpacer(Dimens.SpacingSingleHalf)
        if (extracting) {
            AppDotsPulsingLoading(colorArgb = AppTheme.colorScheme.main.toArgb())
        } else {
            Text(
                text = stringResource(R.string.extract_text_action),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.colorScheme.text
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun CircularExtractButtonPreview() {
    AppTheme {
        CircularExtractButton(
            onClick = { },
            extracting = false
        )
    }
}
