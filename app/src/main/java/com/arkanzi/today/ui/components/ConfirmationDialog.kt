package com.arkanzi.today.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.arkanzi.today.R
import com.arkanzi.today.ui.theme.ComfortaaFontFamily

@Composable
fun ConfirmationDialog(
    showDialog: Boolean,
    title: String = "Delete Note",
    message: String = "Are you sure you want to delete this note? This action cannot be undone.",
    confirmButtonText: String = "Delete",
    dismissButtonText: String = "Cancel",
    icon: Int = R.drawable.ic_trash,
    iconTint: Color = Color(0xFFCC3558),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Delete",
                    tint = iconTint
                )
            },
            title = {
                Text(
                    text = title,
                    fontFamily = ComfortaaFontFamily,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontFamily = ComfortaaFontFamily,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(
                        text = confirmButtonText,
                        color = iconTint,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ComfortaaFontFamily
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = dismissButtonText,
                        fontFamily = ComfortaaFontFamily
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        showDialog = true,
        onConfirm = { },
        onDismiss = { }
    )
}
