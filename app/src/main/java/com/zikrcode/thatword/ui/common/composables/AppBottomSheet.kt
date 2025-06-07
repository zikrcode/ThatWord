package com.zikrcode.thatword.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zikrcode.thatword.ui.common.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    visible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier.padding(WindowInsets.statusBars.asPaddingValues()),
            sheetState = sheetState,
            containerColor = AppTheme.colorScheme.background,
            dragHandle = {
                BottomSheetDefaults.DragHandle(color = AppTheme.colorScheme.icon)
            },
            contentWindowInsets = { WindowInsets.navigationBars },
            content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun AppBottomSheetPreview() {
    AppTheme {
        var isVisible by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
           Button(onClick = { isVisible = !isVisible }) {
               Text(text = "Open Bottom Sheet")
           }
           AppBottomSheet(
               visible = isVisible,
               sheetState = rememberModalBottomSheetState(),
               onDismiss = { isVisible = false },
               content = {
                   Box(modifier = Modifier.height(400.dp))
               }
           )
        }
    }
}