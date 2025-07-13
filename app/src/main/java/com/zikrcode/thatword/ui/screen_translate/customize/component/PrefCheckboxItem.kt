package com.zikrcode.thatword.ui.screen_translate.customize.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun PrefCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  onCheckedChange.invoke(!checked) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(StyleItemHeight)
                .padding(horizontal = Dimens.SpacingDoubleHalf),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = AppTheme.colorScheme.text,
                style = MaterialTheme.typography.bodyMedium,
            )
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors().copy(
                    checkedCheckmarkColor = AppTheme.colorScheme.container,
                    uncheckedCheckmarkColor = AppTheme.colorScheme.container,
                    checkedBoxColor = AppTheme.colorScheme.main,
                    uncheckedBoxColor = AppTheme.colorScheme.container,
                    checkedBorderColor = AppTheme.colorScheme.main,
                    uncheckedBorderColor = AppTheme.colorScheme.icon
                )
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PrefCheckboxItemPreview() {
    AppTheme {
        Column(
            modifier = Modifier.background(AppTheme.colorScheme.container)
        ) {
            var checked by remember { mutableStateOf(true) }
            PrefCheckboxItem(
                text = "Label",
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
    }
}