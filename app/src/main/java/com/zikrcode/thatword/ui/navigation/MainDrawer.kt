package com.zikrcode.thatword.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppHorizontalDivider
import com.zikrcode.thatword.ui.common.composables.AppVerticalSpacer
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun MainDrawer(
    drawerState: DrawerState,
    currentRoute: Any,
    onNavigateToScreenTranslate: () -> Unit,
    onNavigateToTranslate: () -> Unit,
    closeDrawer: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val drawerWidth = screenWidth * 4 / 5

    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = Modifier
            .width(drawerWidth)
            .fillMaxHeight(),
        drawerContainerColor = AppTheme.colorScheme.background
    ) {
        ThatWordLogo()
        AppHorizontalDivider()
        AppVerticalSpacer(Dimens.SpacingSingleHalf)
        ModalDrawerItem(
            labelRes = R.string.screen_translate,
            selected = currentRoute == ScreenTranslate,
            onClick = onNavigateToScreenTranslate,
            closeDrawer = closeDrawer,
            iconRes = R.drawable.ic_screen_translate,
            iconContentDescriptionRes = R.string.screen_translate_icon
        )
        ModalDrawerItem(
            labelRes = R.string.translate,
            selected = currentRoute == Translate,
            onClick = onNavigateToTranslate,
            closeDrawer = closeDrawer,
            iconRes = R.drawable.ic_translate,
            iconContentDescriptionRes = R.string.translate_icon
        )
    }
}


@PreviewLightDark
@Composable
private fun MainDrawerPreview() {
    AppTheme {
        MainDrawer(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            currentRoute = ScreenTranslate,
            onNavigateToScreenTranslate = { },
            onNavigateToTranslate = { },
            closeDrawer = { }
        )
    }
}

@Composable
private fun ThatWordLogo() {
    Text(
        text = stringResource(R.string.app_name),
        modifier = Modifier.padding(Dimens.SpacingDouble),
        color = AppTheme.colorScheme.main,
        fontSize = 25.sp,
        fontWeight = FontWeight.Black,
    )
}

@Composable
private fun ModalDrawerItem(
    @StringRes labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    closeDrawer: () -> Unit,
    @DrawableRes iconRes: Int,
    @StringRes iconContentDescriptionRes: Int
) {
    NavigationDrawerItem(
        label = {
            Text(text = stringResource(labelRes))
        },
        selected = selected,
        onClick = {
            onClick.invoke()
            closeDrawer.invoke()
        },
        modifier = Modifier.padding(horizontal = Dimens.SpacingSingleHalf),
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = stringResource(iconContentDescriptionRes),
                modifier = Modifier.size(Dimens.SpacingTriple)
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = AppTheme.colorScheme.container,
            unselectedContainerColor = AppTheme.colorScheme.background,
            selectedIconColor = AppTheme.colorScheme.icon,
            unselectedIconColor = AppTheme.colorScheme.icon,
            selectedTextColor = AppTheme.colorScheme.text,
            unselectedTextColor = AppTheme.colorScheme.text
        )
    )
}