package com.zikrcode.thatword.ui.screen_translate.customize.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zikrcode.thatword.ui.common.composables.AppVerticalSpacer
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

val StyleItemHeight = 60.dp

@Composable
fun PrefWithLabelContainer(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(Dimens.SpacingDouble)
            )
            .background(AppTheme.colorScheme.container),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingDouble)
        ) {
            Text(
                text = label,
                color = AppTheme.colorScheme.main,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            AppVerticalSpacer(Dimens.SpacingDouble)
            content()
        }
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