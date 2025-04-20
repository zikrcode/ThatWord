package com.zikrcode.thatword.ui.common.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    openDrawer: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = openDrawer,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = AppTheme.colorScheme.text,
                    containerColor = AppTheme.colorScheme.background
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.open_drawer)
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
           openDrawer = { }
       )
    }
}