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


@OptIn(ExperimentalMaterial3Api::class)
object FutureOrTodaySelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val now = System.currentTimeMillis()
        // Allow today and future; normalize both to date-only if needed
        return utcTimeMillis >= startOfTodayUtc()
    }
    override fun isSelectableYear(year: Int): Boolean {
        val currentYear = java.time.LocalDate.now().year
        return year >= currentYear
    }
    private fun startOfTodayUtc(): Long {
        val today = java.time.LocalDate.now(java.time.ZoneOffset.UTC)
        return today.atStartOfDay(java.time.ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogCustom(
    show: Boolean,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long? = null,
    onDateSelected: (Long) -> Unit
) {
    if (!show) return
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        selectableDates = FutureOrTodaySelectableDates
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


@Preview
@Composable
fun DatePickerDialogCustomPreview(){
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { showDialog = true }) {
            Text("Open TimePickerDialog")
        }
        DatePickerDialogCustom(show = showDialog, onDismiss = {showDialog = false }, onDateSelected = {})
    }
}