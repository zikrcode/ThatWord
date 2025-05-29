package com.zikrcode.thatword.ui.common.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    navIcon: Painter,
    navIconDescription: String,
    onNavIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onNavIconClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = AppTheme.colorScheme.icon,
                    containerColor = AppTheme.colorScheme.background
                )
            ) {
                Icon(
                    painter = navIcon,
                    contentDescription = navIconDescription
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colorScheme.background,
            titleContentColor = AppTheme.colorScheme.text,
        )
    )
}

@PreviewLightDark
@Composable
private fun AppTopBarPreview() {
    AppTheme {
       AppTopBar(
           title = "ThatWord",
           navIcon = painterResource(R.drawable.ic_menu),
           navIconDescription = stringResource(R.string.open_drawer),
           onNavIconClick = { }
       )
    }
}