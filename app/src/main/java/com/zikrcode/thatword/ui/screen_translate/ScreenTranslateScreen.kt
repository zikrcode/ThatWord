package com.zikrcode.thatword.ui.screen_translate

import android.Manifest
import android.os.Build
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.screen_translate.component.CircularPowerButton
import com.zikrcode.thatword.ui.theme.ThatWordTheme
import com.zikrcode.thatword.ui.utils.Permissions
import com.zikrcode.thatword.ui.utils.RequestPermission
import com.zikrcode.thatword.ui.utils.composables.AppAlertDialog

@Composable
fun ScreenTranslateScreen(
    openDrawer: () -> Unit,
    viewModel: ScreenTranslateViewModel = hiltViewModel()
) {

    // request for notifications permission from Android 13 (API level 33) onwards
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val context = LocalContext.current
        val permission = Manifest.permission.POST_NOTIFICATIONS

        if (!Permissions.checkPermission(context, permission)) {
            RequestPermission(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                onResult = {
                    /*
                    we can safely ignore the result because app functions properly even
                    without notifications permission
                     */
                }
            )
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenTranslateScreenContent(
        openDrawer = openDrawer,
        refreshServiceStatus = viewModel::refreshServiceStatus,
        isServiceRunning = uiState.isServiceRunning,
        startService = viewModel::startService,
        stopService = viewModel::stopService,
    )
}

@Preview(showBackground = true)
@Composable
fun ScreenTranslateScreenContentPreview() {
    ThatWordTheme {
        ScreenTranslateScreenContent(
            openDrawer = { },
            refreshServiceStatus = { },
            isServiceRunning = true,
            startService = { },
            stopService = { }
        )
    }
}

@Composable
private fun ScreenTranslateScreenContent(
    openDrawer: () -> Unit,
    refreshServiceStatus: () -> Unit,
    isServiceRunning: Boolean,
    startService: () -> Unit,
    stopService: () -> Unit
) {
    val context = LocalContext.current
    var hasDrawOverlayPermission by remember {
        // initial value is false because we will set it ON_RESUME
        mutableStateOf(false)
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        hasDrawOverlayPermission = Permissions.checkDrawOverlayPermission(context)
        refreshServiceStatus()
    }
    var requestDrawOverlayPermission by remember {
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
                    if (hasDrawOverlayPermission) {
                        if (isServiceRunning) stopService() else startService()
                    } else {
                        requestDrawOverlayPermission = true
                    }
                },
                mainColor = if (isServiceRunning) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Red
                }
            )
        }
        if (requestDrawOverlayPermission) {
            AppAlertDialog(
                title = stringResource(R.string.draw_overlay_permission_request_title),
                text = stringResource(R.string.draw_overlay_permission_request_text),
                confirmButtonText = stringResource(R.string.ok),
                onConfirmClick = {
                    Permissions.requestDrawOverlayPermission(context)
                    requestDrawOverlayPermission = false
                },
                dismissButtonText = stringResource(R.string.cancel),
                onDismissClick = { requestDrawOverlayPermission = false }
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
