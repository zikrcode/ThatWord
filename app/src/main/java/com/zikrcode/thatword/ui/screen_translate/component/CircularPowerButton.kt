package com.zikrcode.thatword.ui.screen_translate.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppVerticalSpacer
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

val CircularPowerButtonSize = 140.dp

@Composable
fun CircularPowerButton(
    turnedOn: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (turnedOn) AppTheme.colorScheme.red else AppTheme.colorScheme.main
    val iconDrawableRes = if (turnedOn) R.drawable.ic_stop else R.drawable.ic_play_arrow
    val labelStringRes = if (turnedOn) R.string.stop else R.string.start

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
                color = contentColor,
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
            painter = painterResource(iconDrawableRes),
            contentDescription = stringResource(labelStringRes),
            modifier = Modifier.size(30.dp),
            tint = contentColor
        )
        AppVerticalSpacer(Dimens.SpacingSingleHalf)
        Text(
            text = stringResource(labelStringRes),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.text
        )
    }
}

@PreviewLightDark
@Composable
private fun CircularPowerButtonPreview() {
    AppTheme {
        var isRunning by remember { mutableStateOf(false) }
        CircularPowerButton(
            turnedOn = isRunning,
            onClick = { isRunning = !isRunning }
        )
    }
}
