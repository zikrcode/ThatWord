package com.zikrcode.thatword.ui.extract_text

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.thatword.R
import com.zikrcode.thatword.provider.MainFileProvider
import com.zikrcode.thatword.ui.common.composables.AppScreenContent
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.ui.extract_text.component.ExtractTextButton
import com.zikrcode.thatword.ui.extract_text.component.ExtractTextButtonPosition
import com.zikrcode.thatword.ui.extract_text.component.ImageContent
import com.zikrcode.thatword.utils.Dimens

@Composable
fun ExtractTextScreen(
    onOpenDrawer: () -> Unit,
    viewModel: ExtractTextViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tempImageUri = MainFileProvider.createTempImageUri(context)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExtractTextScreenContent(
        onOpenDrawer = onOpenDrawer,
        tempImageUri = tempImageUri,
        imageUri = uiState.imageUri,
        isExtracting = uiState.isExtracting,
        extractedText = uiState.extractedText,
        onEvent = viewModel::onEvent
    )
}

@PreviewLightDark
@Composable
private fun ExtractTextScreenContentPreview() {
    AppTheme {
        ExtractTextScreenContent(
            onOpenDrawer = { },
            tempImageUri = Uri.EMPTY,
            imageUri = Uri.EMPTY,
            isExtracting = false,
            extractedText = null,
            onEvent = { }
        )
    }
}

@Composable
private fun ExtractTextScreenContent(
    onOpenDrawer: () -> Unit,
    tempImageUri: Uri,
    imageUri: Uri?,
    isExtracting: Boolean,
    extractedText: String?,
    onEvent: (ExtractTextUiEvent) -> Unit
) {
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onEvent.invoke(ExtractTextUiEvent.ChangeImage(tempImageUri))
            }
        }
    )
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imageUri ->
            imageUri?.let { uri ->
                onEvent.invoke(ExtractTextUiEvent.ChangeImage(uri))
            }
        }
    )

    AppScreenContent(
        navIcon = painterResource(R.drawable.ic_menu),
        navIconDescription = stringResource(R.string.open_drawer),
        onNavIconClick = onOpenDrawer,
        title = stringResource(R.string.extract_text)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = Dimens.SpacingDoubleHalf,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.7f),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingHalf)
            ) {
                ExtractTextButton(
                    position = ExtractTextButtonPosition.LEFT,
                    icon = painterResource(R.drawable.ic_take_image),
                    label = stringResource(R.string.take_image),
                    onClick = {
                        cameraLauncher.launch(tempImageUri)
                    },
                    modifier = Modifier.weight(1f)
                )
                ExtractTextButton(
                    position = ExtractTextButtonPosition.RIGHT,
                    icon = painterResource(R.drawable.ic_select_image),
                    label = stringResource(R.string.select_image),
                    onClick = {
                        val request = PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                        imagePickerLauncher.launch(request)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            AnimatedVisibility(visible = imageUri != null) {
                imageUri?.let { uri ->
                    ImageContent(
                        imageUri = uri,
                        onExtractText = {
                            onEvent.invoke(ExtractTextUiEvent.ExtractText)
                        },
                        isExtracting = isExtracting,
                        extractedText = extractedText
                    )
                }
            }
        }
    }
}
