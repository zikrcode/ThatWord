package com.zikrcode.thatword.ui.utils.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.theme.ThatWordTheme

@Composable
fun AppAlertDialog(
    icon: Painter = painterResource(R.drawable.ic_info),
    title: String,
    text: String,
    confirmButtonText: String,
    onConfirmClick: () -> Unit,
    dismissButtonText: String,
    onDismissClick: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissClick.invoke() },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissClick) {
                Text(text = dismissButtonText)
            }
        },
        icon = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        },
        title = { Text(text = title) },
        text = { Text(text = text) }
    )
}

@Preview
@Composable
fun AppAlertDialogPreview() {
    ThatWordTheme {
        AppAlertDialog(
            icon = painterResource(R.drawable.ic_info),
            title = "Warning",
            text = "This is a warning dialog",
            confirmButtonText = "Confirm",
            onConfirmClick = { },
            dismissButtonText = "Dismiss",
            onDismissClick = { }
        )
    }
}