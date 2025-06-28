package com.zikrcode.thatword.ui.screen_translate.customize.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.sp
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun PrefSwitchItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(StyleItemHeight)
            .clickable {
                onCheckedChange.invoke(!checked)
            }
            .padding(Dimens.SpacingSingleHalf),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = AppTheme.colorScheme.text,
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium,
        )
        Switch(
            checked = checked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors().copy(
                checkedThumbColor = AppTheme.colorScheme.main,
                checkedTrackColor = AppTheme.colorScheme.container,
                checkedBorderColor = AppTheme.colorScheme.text,
                uncheckedThumbColor = AppTheme.colorScheme.text,
                uncheckedTrackColor = AppTheme.colorScheme.container,
                uncheckedBorderColor = AppTheme.colorScheme.text,
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun TextStyleSectionPreview() {
    AppTheme {
        Column(
            modifier = Modifier.background(AppTheme.colorScheme.background)
        ) {
            PrefSwitchItem(
                text = "Label",
                checked = true,
                onCheckedChange = { }
            )
            PrefSwitchItem(
                text = "Label",
                checked = false,
                onCheckedChange = { }
            )
        }
    }
}