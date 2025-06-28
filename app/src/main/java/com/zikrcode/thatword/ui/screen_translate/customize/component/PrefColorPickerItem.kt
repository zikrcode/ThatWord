package com.zikrcode.thatword.ui.screen_translate.customize.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.composables.AppBottomSheet
import com.zikrcode.thatword.ui.common.theme.AppColor
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.ui.utils.AppConstants
import com.zikrcode.thatword.utils.Dimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrefColorPickerItem(
    text: String,
    colorArgb: Int,
    onColorArgbChange: (Int) -> Unit
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(StyleItemHeight)
            .clickable { showBottomSheet = true }
            .padding(horizontal = Dimens.SpacingSingleHalf),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = AppTheme.colorScheme.text,
            fontSize = 18.sp,
            style = MaterialTheme.typography.titleMedium,
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = AppTheme.colorScheme.text,
                    shape = CircleShape
                )
                .background(Color(colorArgb))
                .size(Dimens.SpacingQuadruple)
        )
    }

    val colorsArgb = AppColor.selectableColorsArgb

    AppBottomSheet(
        visible = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        sheetState = sheetState
    ) {
        Text(
            text = stringResource(R.string.select_color),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingSingleHalf),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = AppTheme.colorScheme.text
        )
        LazyRow(
            modifier = Modifier.verticalScroll(state = rememberScrollState()),
            contentPadding = PaddingValues(Dimens.SpacingSingleHalf),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSingleHalf),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(colorsArgb.toList()) { argb ->
                CircularColorButton(
                    color = Color(argb),
                    selected = Color(argb) == Color(colorArgb),
                    onClick = {
                        onColorArgbChange.invoke(argb)
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

@Composable
private fun CircularColorButton(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = AppTheme.colorScheme.divider,
                shape = CircleShape
            )
            .background(color)
            .size(Dimens.SpacingSextuple)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = AppTheme.colorScheme.divider
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PrefColorPickerItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier.background(AppTheme.colorScheme.background)
        ) {
            PrefColorPickerItem(
                text = "Label",
                colorArgb = AppConstants.DEFAULT_TEXT_BACKGROUND_COLOR_ARGB,
                onColorArgbChange = { }
            )
        }
    }
}