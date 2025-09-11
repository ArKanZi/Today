package com.arkanzi.today.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkanzi.today.R

@Composable
fun InputFields(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.ic_notes_extra,
    isIcon: Boolean = true,
    description: String = "",
    label: String,
    tint: Color,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    isEnabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    value: String = "",
    onValueChange: (String) -> Unit = { },
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if(isIcon){
            Column(modifier = Modifier.padding(end = 8.dp)) {
                IconContainer(
                    icon = icon,
                    description = description,
                    modifier = Modifier,
                    tint = tint,
                    onClick = {}
                )
            }
        }
        Column(modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = value,
                    onValueChange = { onValueChange(it) },
                    label = { Text(label, style = MaterialTheme.typography.bodySmall) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = singleLine,
                    readOnly = readOnly,
                    enabled = isEnabled,


                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    trailingIcon = trailingIcon,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    }
}

@Preview
@Composable
fun InputFieldsPreview() {
    InputFields(
        icon=R.drawable.ic_notes,
        isIcon = false,
        label = "Add a note",
        modifier = Modifier.background(Color.White),
        tint = Color(0xFF8695BE),
        value = "something",
        onValueChange = {})
}