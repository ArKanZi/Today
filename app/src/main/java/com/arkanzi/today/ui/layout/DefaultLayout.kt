package com.arkanzi.today.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun DefaultLayout(
    chrome: ScreenChrome,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { chrome.topBar?.invoke() },
        bottomBar = { chrome.bottomBar?.invoke() },
        containerColor = containerColor,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp)
            ){content()}

        })
}

@Preview
@Composable
fun DefaultLayoutPreview() {
        DefaultLayout(
            chrome = ScreenChrome(
            topBar = {},
            bottomBar = { Text("Bottom Bar") }),
            content = { Text("Content", color = MaterialTheme.colorScheme.error) })
}