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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.screen_translate.component.CircularPowerButton
import com.zikrcode.thatword.ui.theme.ThatWordTheme
import com.zikrcode.thatword.ui.utils.MediaProjectionToken
import com.zikrcode.thatword.ui.utils.Permissions
import com.zikrcode.thatword.ui.utils.composables.AppAlertDialog

@Composable
fun ScreenTranslateScreen(
    openDrawer: () -> Unit,
    viewModel: ScreenTranslateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenTranslateScreenContent(
        openDrawer = openDrawer,
        isServiceRunning = uiState.isServiceRunning,
        startService = viewModel::startService,
        stopService = viewModel::stopService
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenTranslateScreenContentPreview() {
    ThatWordTheme {
        ScreenTranslateScreenContent(
            openDrawer = { },
            isServiceRunning = true,
            startService = { },
            stopService = { }
        )
    }
}

@Composable
private fun ScreenTranslateScreenContent(
    openDrawer: () -> Unit,
    isServiceRunning: Boolean,
    startService: (MediaProjectionToken) -> Unit,
    stopService: () -> Unit
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
            ScreenTranslateTopBar(openDrawer)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularPowerButton(
                text = stringResource(
                    if (isServiceRunning) R.string.on else R.string.off
                ),
                onClick = {
                    when {
                        isServiceRunning -> {
                            stopService()
                        }
                        Permissions.checkDrawOverlayPermission(context) -> {
                            startServiceWithMediaProjection = true
                        }
                        else -> {
                            requestDrawOverlayPermission = true
                        }
                    }
                },
                mainColor = if (isServiceRunning) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Red
                }
            )
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
                    if (token != null) startService(token)
                    startServiceWithMediaProjection = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTranslateTopBar(openDrawer: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.screen_translate))
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.open_drawer)
                )
            }
        }
    )
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
private fun MediaProjectionTokenPermission(onResult: (MediaProjectionToken?) -> Unit, ) {
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