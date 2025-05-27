package com.zikrcode.thatword.ui.screen_translate

import android.Manifest
import android.app.Activity.RESULT_OK
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.thatword.R
import com.zikrcode.thatword.domain.models.Language
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.ui.utils.MediaProjectionToken
import com.zikrcode.thatword.ui.utils.Permissions
import com.zikrcode.thatword.ui.common.composables.AppAlertDialog
import com.zikrcode.thatword.ui.common.composables.AppContentLoading
import com.zikrcode.thatword.ui.common.composables.AppTopBar
import com.zikrcode.thatword.ui.screen_translate.component.AppearanceBoxCard
import com.zikrcode.thatword.ui.screen_translate.component.CentralBoxCard

@Composable
fun ScreenTranslateScreen(
    onOpenDrawer: () -> Unit,
    onOpenAppearance: () -> Unit,
    viewModel: ScreenTranslateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenTranslateScreenContent(
        onOpenDrawer = onOpenDrawer,
        onOpenAppearance = onOpenAppearance,
        isLoading = uiState.isLoading,
        isServiceRunning = uiState.isServiceRunning,
        languages = uiState.supportedLanguages,
        inputLanguage = uiState.inputLanguage,
        outputLanguage = uiState.outputLanguage,
        onEvent = viewModel::onEvent,
    )
}

@PreviewLightDark
@Composable
private fun ScreenTranslateScreenContentPreview() {
    AppTheme {
        ScreenTranslateScreenContent(
            onOpenDrawer = { },
            onOpenAppearance = { },
            isLoading = false,
            isServiceRunning = true,
            languages = listOf(Language("en"), Language("ru")),
            inputLanguage = Language("en"),
            outputLanguage = Language("ru"),
            onEvent = { }
        )
    }
}

@Composable
private fun ScreenTranslateScreenContent(
    onOpenDrawer: () -> Unit,
    onOpenAppearance: () -> Unit,
    isLoading: Boolean,
    isServiceRunning: Boolean,
    languages: List<Language>,
    inputLanguage: Language?,
    outputLanguage: Language?,
    onEvent: (ScreenTranslateUiEvent) -> Unit,
) {
    val context = LocalContext.current
    var requestDrawOverlayPermission by remember {
        mutableStateOf(false)
    }
    var startServiceWithMediaProjection by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.screen_translate),
                openDrawer = onOpenDrawer
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
            if (languages.isEmpty() || inputLanguage == null || outputLanguage == null) {
                // in real scenario we should have all content when isLoading is false but to prevent NPE
                return@Scaffold
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CentralBoxCard(
                    expanded = isServiceRunning,
                    onClick = {
                        when {
                            isServiceRunning -> {
                                onEvent.invoke(ScreenTranslateUiEvent.StopService)
                            }
                            Permissions.checkDrawOverlayPermission(context) -> {
                                startServiceWithMediaProjection = true
                            }
                            else -> {
                                requestDrawOverlayPermission = true
                            }
                        }
                    },
                    languages = languages,
                    inputLanguage = inputLanguage,
                    outputLanguage = outputLanguage,
                    onLanguageChange = { language, direction ->
                        onEvent.invoke(
                            ScreenTranslateUiEvent.ChangeLanguage(
                                language = language,
                                direction = direction
                            )
                        )
                    },
                    onSwapLanguage = {
                        onEvent.invoke(ScreenTranslateUiEvent.SwapLanguages)
                    }
                )
                AppearanceBoxCard(onClick = onOpenAppearance)
            }
        }
    }

    // optional permission
    PostNotificationPermission()

    // required permission on first launch
    if (requestDrawOverlayPermission) {
        DrawOverlayPermission(
            onActionSelect = { requestDrawOverlayPermission = false }
        )
    }

    // special required permission on every launch
    if (startServiceWithMediaProjection) {
        MediaProjectionTokenPermission(
            onResult = { token ->
                if (token != null) onEvent.invoke(
                    ScreenTranslateUiEvent.StartService(token)
                )
                startServiceWithMediaProjection = false
            }
        )
    }
}

@Composable
private fun PostNotificationPermission() {
    val context = LocalContext.current

    // request for notifications permission from Android 13 (API level 33) onwards
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        && !Permissions.checkPermission(context, Manifest.permission.POST_NOTIFICATIONS)) {

        val postNotificationLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {
                /*
                we can safely ignore the result because app functions properly even
                without notifications permission
                */
            }
        )

        LaunchedEffect(Unit) {
            postNotificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
private fun DrawOverlayPermission(onActionSelect: () -> Unit) {
    val context = LocalContext.current
    AppAlertDialog(
        title = stringResource(R.string.draw_overlay_permission_request_title),
        text = stringResource(R.string.draw_overlay_permission_request_text),
        confirmButtonText = stringResource(R.string.ok),
        onConfirmClick = {
            Permissions.requestDrawOverlayPermission(context)
            onActionSelect.invoke()
        },
        dismissButtonText = stringResource(R.string.cancel),
        onDismissClick = {
            onActionSelect.invoke()
        }
    )
}

@Composable
private fun MediaProjectionTokenPermission(onResult: (MediaProjectionToken?) -> Unit) {
    val context = LocalContext.current
    val mediaProjectionManager = context.getSystemService(MediaProjectionManager::class.java)
    val mediaProjectionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                onResult.invoke(
                    MediaProjectionToken(
                        resultCode = result.resultCode,
                        resultData = result.data!!
                    )
                )
            } else {
                onResult.invoke(null)
            }
        }
    )

    LaunchedEffect(Unit) {
        mediaProjectionLauncher.launch(
            mediaProjectionManager.createScreenCaptureIntent()
        )
    }
}