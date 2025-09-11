package com.arkanzi.today.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.tooling.preview.Preview




@Composable
fun DefaultLayout(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(topBar = topBar,
        bottomBar = bottomBar,
        containerColor = containerColor,
        content = {innerPadding ->
            content(innerPadding) })
}

@Preview
@Composable
fun DefaultLayoutPreview() {
    DefaultLayout(content = { Text("Content", color = MaterialTheme.colorScheme.error) })
}