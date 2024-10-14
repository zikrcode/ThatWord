package com.zikrcode.thatword.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.utils.Dimens

@Composable
fun MainDrawer(
    currentRoute: Any,
    onNavigateToScreenTranslate: () -> Unit,
    onNavigateToTranslate: () -> Unit,
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet {
        ThatWordIcon(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingDouble)
        )
        NavigationDrawerItem(
            label = {
                Text(text = stringResource(R.string.screen_translate))
            },
            selected = currentRoute == ScreenTranslate,
            onClick = {
                onNavigateToScreenTranslate()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_screen_translate),
                    contentDescription = stringResource(R.string.screen_translate_icon),
                    modifier = Modifier.size(Dimens.SpacingTriple)
                )
            }
        )
        NavigationDrawerItem(
            label = {
                Text(text = stringResource(R.string.translate))
            },
            selected = currentRoute == Translate,
            onClick = {
                onNavigateToTranslate()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_translate),
                    contentDescription = stringResource(R.string.translate_icon),
                    modifier = Modifier.size(Dimens.SpacingTriple)
                )
            }
        )
    }
}

@Composable
private fun ThatWordIcon(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_that_word_logo),
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
        )
    }
}
