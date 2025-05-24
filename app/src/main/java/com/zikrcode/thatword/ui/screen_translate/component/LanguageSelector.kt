package com.zikrcode.thatword.ui.screen_translate.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens
import kotlinx.coroutines.launch
import kotlin.collections.forEach

enum class LanguageDirection {
    Input, Output
}

val LanguageSelectorWidth = 160.dp
val LanguageSelectorHeight = 48.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LanguageSelector(
    direction: LanguageDirection,
    currentLanguage: Language,
    languages: List<Language>,
    onLanguageSelect: (Language) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .size(
                width = LanguageSelectorWidth,
                height = LanguageSelectorHeight
            )
            .shadow(
                elevation = Dimens.ElevationSingleHalf,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable {
                showBottomSheet = true
            }
            .background(AppTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = currentLanguage.displayLanguage(),
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.text
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = AppTheme.colorScheme.background,
            contentWindowInsets = { WindowInsets.statusBars }
        ) {
            Text(
                text = stringResource(
                    when (direction) {
                        LanguageDirection.Input -> R.string.translate_from
                        LanguageDirection.Output -> R.string.translate_to
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingSingleHalf),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                color = AppTheme.colorScheme.text
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.SpacingSingleHalf)
                    .verticalScroll(state = rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSingleHalf),
                verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSingleHalf)
            ) {
                languages.forEach { language ->
                    LanguageGridItem(
                        language = language,
                        currentLanguage = currentLanguage,
                        onClick = {
                            onLanguageSelect.invoke(language)
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageGridItem(
    language: Language,
    currentLanguage: Language,
    onClick: () -> Unit
) {
    val borderColor = if (language == currentLanguage) {
        AppTheme.colorScheme.main
    } else {
        AppTheme.colorScheme.divider
    }

    OutlinedCard(
        onClick = onClick,
        colors = CardDefaults.cardColors().copy(
            containerColor = AppTheme.colorScheme.container
        ),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(Dimens.SpacingSingleHalf),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (language == currentLanguage) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.padding(end = Dimens.SpacingHalf),
                    tint = AppTheme.colorScheme.main
                )
            }
            Text(
                text = language.displayLanguage(),
                color = AppTheme.colorScheme.text
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun LanguageSelectorPreview() {
    AppTheme {
        var currentLanguage by remember {
            mutableStateOf(Language("en"))
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LanguageSelector(
                direction = LanguageDirection.Input,
                currentLanguage = currentLanguage,
                languages = listOf(
                    Language("en"),
                    Language("ru"),
                    Language("de"),
                    Language("fr"),
                    Language("es"),
                    Language("it"),
                    Language("pt"),
                    Language("ja")
                ),
                onLanguageSelect = { language ->
                    currentLanguage = language
                }
            )
        }
    }
}