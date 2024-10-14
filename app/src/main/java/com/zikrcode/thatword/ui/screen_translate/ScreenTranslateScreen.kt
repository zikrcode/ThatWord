package com.zikrcode.thatword.ui.screen_translate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.theme.ThatWordTheme

@Composable
fun ScreenTranslateScreen(openDrawer: () -> Unit) {
    ScreenTranslateScreenContent(openDrawer)
}

@Preview(showBackground = true)
@Composable
fun ScreenTranslateScreenContentPreview() {
    ThatWordTheme {
        ScreenTranslateScreenContent { }
    }
}

@Composable
private fun ScreenTranslateScreenContent(openDrawer: () -> Unit) {
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
            Text(text = "ScreenTranslateScreenContent")
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
