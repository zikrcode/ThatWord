package com.zikrcode.thatword.ui.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.zikrcode.thatword.R
import com.zikrcode.thatword.ui.common.theme.AppTheme
import com.zikrcode.thatword.utils.Dimens

@Composable
fun AppScreenContent(
    navIcon: Painter,
    navIconDescription: String,
    onNavIconClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = title,
                navIcon = navIcon,
                navIconDescription = navIconDescription,
                onNavIconClick = onNavIconClick
            )
        },
        containerColor = AppTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.SpacingDoubleHalf)
                .padding(bottom = Dimens.SpacingDoubleHalf),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(Dimens.SpacingQuintuple),
                    color = AppTheme.colorScheme.main,
                    trackColor = Color.Transparent,
                )
            } else {
                content()
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AppScreenContentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            AppScreenContent(
                title = "Screen Title",
                navIcon = painterResource(R.drawable.ic_menu),
                navIconDescription = "",
                onNavIconClick = { },
                loading = true,
                content = { }
            )
        }
    }
}