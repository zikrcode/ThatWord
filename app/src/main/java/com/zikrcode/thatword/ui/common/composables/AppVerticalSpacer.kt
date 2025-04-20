package com.zikrcode.thatword.ui.common.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.ui.common.theme.AppTheme

@Composable
fun AppVerticalSpacer(height: Dp) {
    Spacer(Modifier.height(height))
}