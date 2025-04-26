package com.zikrcode.thatword.ui.screen_translate.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun CentralBoxCard(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val boxHeight by animateDpAsState(
        targetValue = if (expanded) {
            CircularPowerButtonSize * 3
        } else {
            CircularPowerButtonSize + (Dimens.SpacingQuadruple * 2)
        },
        animationSpec = centralBoxCardSpringSpec()
    )
    val boxColor by animateColorAsState(
        targetValue = if (expanded) {
            AppTheme.colorScheme.container
        } else {
            AppTheme.colorScheme.background
        },
        animationSpec = centralBoxCardSpringSpec()
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.SpacingQuadruple)
            .height(boxHeight)
            .clip(RoundedCornerShape(Dimens.SpacingDouble))
            .background(boxColor),
        contentAlignment = Alignment.TopCenter
    ) {
        CircularPowerButton(
            turnedOn = expanded,
            onClick = onClick,
            modifier = Modifier.padding(Dimens.SpacingQuadruple)
        )
    }
}

private fun <T> centralBoxCardSpringSpec(): SpringSpec<T> = spring(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessMediumLow
)

@PreviewLightDark
@Composable
private fun CentralBoxCardPreview() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var expanded by remember { mutableStateOf(false) }
            CentralBoxCard(
                expanded = expanded,
                onClick = { expanded = !expanded }
            )
        }
    }
}