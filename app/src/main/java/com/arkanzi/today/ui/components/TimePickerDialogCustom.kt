package com.arkanzi.today.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogCustom(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    initialTimestamp: Long,
    is24Hour: Boolean = false,
    title: String,
    onTimeSelected: (selectedTimestamp: Long) -> Unit,
) {
    val calendar = remember(initialTimestamp) {
        Calendar.getInstance().apply {
            timeInMillis = initialTimestamp
        }
    }

    val timeState = remember(showDialog) {
        if (showDialog) {
            TimePickerState(
                initialHour = calendar.get(Calendar.HOUR_OF_DAY),
                initialMinute = calendar.get(Calendar.MINUTE),
                is24Hour = is24Hour
            )
        } else {
            null
        }
    }

    if (showDialog && timeState != null) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties()
        ) {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(modifier = Modifier.padding(bottom = 20.dp)) {
                        Text(
                            title,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize
                        )
                    }
                    TimePicker(state = timeState)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            val cal = Calendar.getInstance().apply {
                                timeInMillis = initialTimestamp
                                set(Calendar.HOUR_OF_DAY, timeState.hour)
                                set(Calendar.MINUTE, timeState.minute)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            onTimeSelected(cal.timeInMillis)
                            onDismiss()
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimePickerDialogSafePreview() {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { showDialog = true }) {
            Text("Open TimePickerDialog")
        }

        TimePickerDialogCustom(
            showDialog = showDialog,
            onDismiss = { showDialog = false }, // Fixed: was missing = false
            initialTimestamp =17000000000L,
            is24Hour = false,
            onTimeSelected = { /* Handle selected time */ },
            title = "New time"
        )
    }
}
