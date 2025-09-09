package com.zikrcode.thatword.ui.extract_text.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppBottomSheet
import com.zikrcode.thatword.ui.common.extension.appClipCircle
import com.zikrcode.thatword.ui.common.extension.appShadowElevation
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

val ResultButtonWidth = 160.dp
val ResultButtonHeight = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultButton(
    result: String,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by rememberSaveable {
        mutableStateOf(true)
    }

    Box(
        modifier = modifier
            .size(
                width = ResultButtonWidth,
                height = ResultButtonHeight
            )
            .appShadowElevation(CircleShape)
            .appClipCircle()
            .clickable {
                showBottomSheet = true
            }
            .background(
                color = AppTheme.colorScheme.background,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.view_results),
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.text
        )
    }

    AppBottomSheet(
        visible = showBottomSheet,
        sheetState = sheetState,
        onDismiss = { showBottomSheet = false }
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = AppTheme.colorScheme.main,
                backgroundColor = AppTheme.colorScheme.main.copy(alpha = .4f)
            )
        ) {
            BasicTextField(
                value = result,
                onValueChange = { },
                modifier = Modifier
                    .weight(1f)
                    .padding(Dimens.SpacingSingleHalf),
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = AppTheme.colorScheme.text
                )
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ResultButtonPreview() {
    AppTheme {
        ResultButton(result = "Hello World")
    }
}
