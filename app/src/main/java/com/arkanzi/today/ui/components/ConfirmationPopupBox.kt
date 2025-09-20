package com.arkanzi.today.ui.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkanzi.today.R

@Composable
fun ConfirmationPopupBox(
    icon: Int,
    tint: Color,
    content: String,
    leftButtonText: String,
    rightButtonText: String,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().weight(2.5f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            IconContainer(
                icon = icon,
                description = "",
                tint = tint,
                onClick = {})
            Text(content, modifier = Modifier.padding(18.dp))
        }
        Row(modifier = Modifier.fillMaxWidth().weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    leftButtonText
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Text(rightButtonText) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationPopupBoxPreview() {
    ConfirmationPopupBox(
        icon = R.drawable.ic_trash,
        tint = Color(0xFFD07589),
        content = "Are you sure to delete?",
        leftButtonText = "Cancel",
        rightButtonText = "Delete",
        onDismiss = { },
        onClick = {}
    )
}