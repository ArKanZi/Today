package com.arkanzi.today.ui.layout

import androidx.compose.runtime.Composable

data class ScreenChrome(
    val topBar: @Composable (() -> Unit)? = null,
    val bottomBar: @Composable (() -> Unit)? = null
)