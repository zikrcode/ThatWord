package com.zikrcode.thatword.ui.screen_translate.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun CircularPowerButton(
    text: String,
    onClick: () -> Unit,
    mainColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier
            .size(160.dp)
            .shadow(
                elevation = Dimens.ElevationSingleHalf,
                shape = CircleShape
            ),
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = mainColor
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_power),
                contentDescription = stringResource(R.string.power_toggle),
                modifier = Modifier.size(40.dp),
                tint = mainColor
            )
            Spacer(Modifier.height(Dimens.SpacingSingleHalf))
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
fun ScreenTranslateScreenContentPreview() {
    AppTheme {
        CircularPowerButton(
            text = "Off",
            onClick = { },
            mainColor = Color.Red,
        )
    }
}
