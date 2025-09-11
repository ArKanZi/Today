package com.arkanzi.today.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkanzi.today.R

@Composable
fun IconContainer(
    icon: Int,
    description: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.surfaceContainerLowest),
    tint: Color = Color.Black,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = CircleShape,
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        IconButton(
            onClick = onClick,
            modifier = iconModifier
        )
        {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = description,
                modifier = Modifier.background(Color.Transparent),
                tint = tint
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun IconContainerPreview() {
    IconContainer(R.drawable.ic_edit, "", tint = Color(0xFF8695BE))
}