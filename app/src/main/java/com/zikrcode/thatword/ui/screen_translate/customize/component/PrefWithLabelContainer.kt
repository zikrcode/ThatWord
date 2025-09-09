package com.zikrcode.thatword.ui.screen_translate.customize.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.ui.common.composables.AppVerticalSpacer
import com.zikrcode.thatword.ui.common.extension.appClipRoundedCorner
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

val StyleItemHeight = 50.dp

@Composable
fun PrefWithLabelContainer(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = AppTheme.colorScheme.text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        AppVerticalSpacer(Dimens.SpacingDoubleHalf)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .appClipRoundedCorner()
                .background(AppTheme.colorScheme.container),
            content = content
        )
    }
}

@PreviewLightDark
@Composable
private fun PrefWithLabelContainerPreview() {
    AppTheme {
        PrefWithLabelContainer(
            label = "Label",
            content = {
                Box(modifier = Modifier.size(150.dp))
            }
        )
    }
}