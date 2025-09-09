package com.zikrcode.thatword.ui.screen_translate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.extension.appClipRoundedCorner
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun CustomizeBoxCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .appClipRoundedCorner()
            .background(AppTheme.colorScheme.container)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_customize),
            contentDescription = null,
            modifier = Modifier
                .padding(Dimens.SpacingDouble)
                .size(30.dp),
            tint = AppTheme.colorScheme.main
        )
        Text(
            text = stringResource(R.string.customize),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.main
        )
    }
}

@PreviewLightDark
@Composable
private fun AppearanceBoxCardPreview() {
    AppTheme {
        CustomizeBoxCard(
            onClick = { }
        )
    }
}