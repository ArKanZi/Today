package com.arkanzi.today.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview




@Composable
fun DefaultLayout(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(topBar = topBar,
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background,
        content = {innerPadding ->
            content(innerPadding) })
}

@Preview
@Composable
fun DefaultLayoutPreview() {
    DefaultLayout(content = { Text("Content", color = MaterialTheme.colorScheme.error) })
}