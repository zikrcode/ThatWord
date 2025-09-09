package com.zikrcode.thatword.ui.screen_translate.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.common.composables.AppHorizontalDivider
import com.zikrcode.thatword.ui.common.extension.appClipRoundedCorner
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun CentralBoxCard(
    expanded: Boolean,
    onClick: () -> Unit,
    languages: List<Language>,
    inputLanguage: Language,
    outputLanguage: Language,
    onLanguageChange: (Language, LanguageDirection) -> Unit,
    onSwapLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swapButtonSize = Dimens.SpacingSextuple
    val languageSelectorsHeight by animateDpAsState(
        targetValue = if (expanded) {
            LanguageSelectorHeight * 2 + swapButtonSize + Dimens.SpacingQuadruple * 2
        } else {
            0.dp
        },
        animationSpec = centralBoxCardSpringSpec()
    )
    val boxHeight by animateDpAsState(
        targetValue = if (expanded) {
            CircularPowerButtonSize +
                    languageSelectorsHeight +
                    (Dimens.SpacingQuadruple * 2)
        } else {
            CircularPowerButtonSize + (Dimens.SpacingQuadruple * 2)
        },
        animationSpec = centralBoxCardSpringSpec()
    )
    val boxColor by animateColorAsState(
        targetValue = if (expanded) {
            AppTheme.colorScheme.container
        } else {
            AppTheme.colorScheme.background
        },
        animationSpec = centralBoxCardSpringSpec()
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(boxHeight)
            .appClipRoundedCorner()
            .background(boxColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularPowerButton(
            turnedOn = expanded,
            onClick = onClick,
            modifier = Modifier.padding(Dimens.SpacingQuadruple)
        )
        AnimatedVisibility(visible = expanded) {
            AppHorizontalDivider(Modifier.padding(horizontal = Dimens.SpacingDoubleHalf))
        }

        if (languageSelectorsHeight > 0.dp) { // to make sure it is not clickable when height is 0
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(languageSelectorsHeight),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LanguageSelector(
                    direction = LanguageDirection.Input,
                    currentLanguage = inputLanguage,
                    languages = languages,
                    onLanguageSelect = { language ->
                        onLanguageChange.invoke(language, LanguageDirection.Input)
                    }
                )

                IconButton(
                    onClick = onSwapLanguage,
                    modifier = Modifier.size(swapButtonSize)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_swap),
                        contentDescription = null,
                        tint = AppTheme.colorScheme.icon
                    )
                }

                LanguageSelector(
                    direction = LanguageDirection.Output,
                    currentLanguage = outputLanguage,
                    languages = languages,
                    onLanguageSelect = { language ->
                        onLanguageChange.invoke(language, LanguageDirection.Output)
                    }
                )
            }
        }
    }
}

private fun <T> centralBoxCardSpringSpec(): SpringSpec<T> = spring(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessMediumLow
)

@PreviewLightDark
@Composable
private fun CentralBoxCardPreview() {
    AppTheme {
        var expanded by remember { mutableStateOf(true) }
        CentralBoxCard(
            expanded = expanded,
            onClick = { expanded = !expanded },
            languages = emptyList(),
            inputLanguage = Language("en"),
            outputLanguage = Language("ru"),
            onLanguageChange = { _, _ -> },
            onSwapLanguage = { }
        )
    }
}