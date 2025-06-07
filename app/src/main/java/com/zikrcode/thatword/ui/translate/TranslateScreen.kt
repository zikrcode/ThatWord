package com.zikrcode.thatword.ui.translate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppTopBar
import com.zikrcode.thatword.ui.common.theme.AppTheme

@Composable
fun TranslateScreen(onOpenDrawer: () -> Unit) {
    TranslateScreenContent(onOpenDrawer)
}

@PreviewLightDark
@Composable
private fun TranslateScreenContentPreview() {
    AppTheme {
        TranslateScreenContent { }
    }
}

@Composable
private fun TranslateScreenContent(onOpenDrawer: () -> Unit) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.translate),
                navIcon = painterResource(R.drawable.ic_menu),
                navIconDescription = stringResource(R.string.open_drawer),
                onNavIconClick = onOpenDrawer
            )
        },
        containerColor = AppTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TranslateScreenContent",
                color = AppTheme.colorScheme.text
            )
        }
    }
}
