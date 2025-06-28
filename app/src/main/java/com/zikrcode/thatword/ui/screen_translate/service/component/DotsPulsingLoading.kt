package com.zikrcode.thatword.ui.screen_translate.service.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

private const val NumberOfDots = 3
private const val DelayUnit = 400
private const val Duration = NumberOfDots * DelayUnit
private val DotSize = Dimens.SpacingSingle

@Composable
fun DotsPulsingLoading(modifier: Modifier = Modifier) {
    val scales = arrayListOf<State<Float>>()
    repeat(NumberOfDots) { index ->
        scales.add(animateScaleWithDelay(delay = index * DelayUnit))
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        scales.forEach { scale ->
            Box(
                Modifier
                    .size(DotSize)
                    .scale(scale.value)
                    .background(
                        color = AppTheme.colorScheme.main,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun animateScaleWithDelay(delay: Int): State<Float> {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = DelayUnit * NumberOfDots
                0f at delay using LinearEasing
                1f at delay + DelayUnit using LinearEasing
                0f at delay + Duration
            }
        )
    )
}

@PreviewLightDark
@Composable
private fun DotsPulsingLoadingPreview() {
    AppTheme {
        DotsPulsingLoading()
    }
}