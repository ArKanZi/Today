package com.arkanzi.today.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomCircleCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 32
) {
    val chekedColor = Brush.linearGradient(
        colors = listOf(Color(0xFF2EB4C8), Color(0xFF5CE487))
    )
    val uncheckedColor = Brush.linearGradient(
        colors = listOf(MaterialTheme.colorScheme.surfaceBright, MaterialTheme.colorScheme.surfaceBright)
    )

    Box(
        modifier = modifier
            .size(size.dp).padding(1.dp)
            .shadow(1.dp, shape = CircleShape, ambientColor = DefaultShadowColor, spotColor = DefaultShadowColor)
            .clip(CircleShape)
            .background(if (checked) chekedColor else uncheckedColor)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Checked",
                tint = if (checked) Color.White else Color.LightGray,
                modifier = Modifier.size((size * 0.6).dp)
            )

    }
}

@Preview(showBackground = true)
@Composable
fun CustomCircleCheckboxPreview(){
    var checked by remember { mutableStateOf(false) }
    CustomCircleCheckbox(checked,{ checked = it })
}