package com.zikrcode.thatword.ui.screen_translate.appearance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppTopBar
import com.zikrcode.thatword.ui.common.theme.AppTheme

@Composable
fun AppearanceScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.appearance),
                navIcon = painterResource(R.drawable.ic_arrow_back),
                navIconDescription = stringResource(R.string.navigate_back),
                onNavIconClick = onBack
            )
        },
        containerColor = AppTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

        }
    }
}

@PreviewLightDark
@Composable
private fun AppearanceScreenContentPreview() {
    AppTheme {
        AppearanceScreen(
            onBack = { }
        )
    }
}