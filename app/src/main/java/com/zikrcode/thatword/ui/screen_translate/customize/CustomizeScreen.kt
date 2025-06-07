package com.zikrcode.thatword.ui.screen_translate.customize

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppContentLoading
import com.zikrcode.thatword.ui.common.composables.AppTopBar
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.ui.screen_translate.customize.component.TextStyleSection
import com.zikrcode.thatword.utils.Dimens

@Composable
fun CustomizeScreen(
    onBack: () -> Unit,
    viewModel: CustomizeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CustomizeContent(
        onBack = onBack,
        isLoading = uiState.isLoading,
        textColorArgb = uiState.textColorArgb,
        textBackgroundColorArgb = uiState.textBackgroundColorArgb,
        uppercaseText = uiState.uppercaseText,
        onEvent = viewModel::onEvent
    )
}

@PreviewLightDark
@Composable
private fun CustomizeScreenContentPreview() {
    AppTheme {
        CustomizeContent(
            onBack = { },
            isLoading = false,
            textColorArgb = Color.Red.toArgb(),
            textBackgroundColorArgb = Color.Yellow.toArgb(),
            uppercaseText = false,
            onEvent = { }
        )
    }
}

@Composable
private fun CustomizeContent(
    onBack: () -> Unit,
    isLoading: Boolean,
    textColorArgb: Int,
    textBackgroundColorArgb: Int,
    uppercaseText: Boolean,
    onEvent: (CustomizeUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.customize),
                navIcon = painterResource(R.drawable.ic_arrow_back),
                navIconDescription = stringResource(R.string.navigate_back),
                onNavIconClick = onBack
            )
        },
        containerColor = AppTheme.colorScheme.background
    ) { paddingValues ->
        if (isLoading) {
            AppContentLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = Dimens.SpacingDouble)
            ) {
                TextStyleSection(
                    textColorArgb = textColorArgb,
                    onTextColorArgbChange = { argb ->
                        onEvent.invoke(CustomizeUiEvent.ChangeTextColor(argb))
                    },
                    textBackgroundColorArgb = textBackgroundColorArgb,
                    onTextBackgroundColorArgbChange = { argb ->
                        onEvent.invoke(CustomizeUiEvent.ChangeTextBackgroundColor(argb))
                    },
                    uppercaseText = uppercaseText,
                    onUppercaseTextChange = { uppercase ->
                        onEvent.invoke(CustomizeUiEvent.ChangeUppercaseText(uppercase))
                    }
                )
            }
        }
    }
}