package com.arkanzi.today.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.arkanzi.today.util.CustomSelectableDates
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogCustom(
    show: Boolean,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long = LocalDate.now().atStartOfDay(ZoneOffset.UTC)
        .toInstant().toEpochMilli(),
    selectableDates: SelectableDates = CustomSelectableDates(),
    onDateSelected: (Long) -> Unit
) {
    if (!show) return
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        selectableDates = selectableDates
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = state.selectedDateMillis != null,
                onClick = {
                    state.selectedDateMillis?.let(onDateSelected)
                    onDismiss()
                }
            ) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    ) {
        DatePicker(state = state)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DatePickerDialogCustomPreview() {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { showDialog = true }) {
            Text("Open TimePickerDialog")
        }
        DatePickerDialogCustom(
            show = showDialog,
            onDismiss = { showDialog = false },
            initialSelectedDateMillis = 1704067200000,
            onDateSelected = {})
    }
}