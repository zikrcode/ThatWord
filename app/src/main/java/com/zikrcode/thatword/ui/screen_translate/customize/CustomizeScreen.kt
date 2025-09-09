package com.zikrcode.thatword.ui.screen_translate.customize

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppScreenContent
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.ui.screen_translate.customize.component.PrefColorPickerItem
import com.zikrcode.thatword.ui.screen_translate.customize.component.PrefCheckboxItem
import com.zikrcode.thatword.ui.screen_translate.customize.component.PrefWithLabelContainer
import com.zikrcode.thatword.ui.utils.AppConstants
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
        iconColorArgb = uiState.iconColorArgb,
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
            iconColorArgb = AppConstants.DEFAULT_ICON_COLOR_ARGB,
            textColorArgb = AppConstants.DEFAULT_TEXT_COLOR_ARGB,
            textBackgroundColorArgb = AppConstants.DEFAULT_TEXT_BACKGROUND_COLOR_ARGB,
            uppercaseText = false,
            onEvent = { }
        )
    }
}

@Composable
private fun CustomizeContent(
    onBack: () -> Unit,
    isLoading: Boolean,
    iconColorArgb: Int,
    textColorArgb: Int,
    textBackgroundColorArgb: Int,
    uppercaseText: Boolean,
    onEvent: (CustomizeUiEvent) -> Unit
) {
    AppScreenContent(
        navIcon = painterResource(R.drawable.ic_arrow_back),
        navIconDescription = stringResource(R.string.navigate_back),
        onNavIconClick = onBack,
        title = stringResource(R.string.customize),
        loading = isLoading
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingQuintuple),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconStyleSection(
                iconColorArgb = iconColorArgb,
                onIconColorArgbChange = { argb ->
                    onEvent.invoke(CustomizeUiEvent.ChangeIconColor(argb))
                }
            )
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

@Composable
private fun IconStyleSection(
    iconColorArgb: Int,
    onIconColorArgbChange: (Int) -> Unit
) {
    PrefWithLabelContainer(label = stringResource(R.string.icon_style)) {
        PrefColorPickerItem(
            text = stringResource(R.string.icon_color),
            colorArgb = iconColorArgb,
            onColorArgbChange = onIconColorArgbChange
        )
    }
}

@Composable
private fun TextStyleSection(
    textColorArgb: Int,
    onTextColorArgbChange: (Int) -> Unit,
    textBackgroundColorArgb: Int,
    onTextBackgroundColorArgbChange: (Int) -> Unit,
    uppercaseText: Boolean,
    onUppercaseTextChange: (Boolean) -> Unit
) {
    PrefWithLabelContainer(label = stringResource(R.string.text_style)) {
        PrefColorPickerItem(
            text = stringResource(R.string.text_color),
            colorArgb = textColorArgb,
            onColorArgbChange = onTextColorArgbChange
        )
        PrefColorPickerItem(
            text = stringResource(R.string.background_color),
            colorArgb = textBackgroundColorArgb,
            onColorArgbChange = onTextBackgroundColorArgbChange
        )
        PrefCheckboxItem(
            text = stringResource(R.string.uppercase_text),
            checked = uppercaseText,
            onCheckedChange = onUppercaseTextChange
        )
    }
}