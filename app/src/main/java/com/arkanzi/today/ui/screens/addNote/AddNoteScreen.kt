package com.arkanzi.today.ui.screens.addNote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryEditable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import com.arkanzi.today.R
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.DatePickerDialogCustom
import com.arkanzi.today.ui.components.TimePickerDialogCustom
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.util.displayDate
import com.arkanzi.today.util.displayTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNotesScreen(
    backStack: NavBackStack,
    noteRepository: NoteRepository,
    calendarTypeRepository: CalendarTypeRepository
) {
    val viewModel: AddNoteViewModel = viewModel(
        factory = AddNoteViewModelFactory(noteRepository, calendarTypeRepository)
    )

    DefaultLayout(
        topBar = {
            TopAppBarCustom(
                leftContent = {
                    IconButton(
                        onClick = { backStack.removeLastOrNull() },
                        modifier = Modifier
                            .size(24.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross),
                            contentDescription = "Add To Do",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)

                        )
                    }
                },
                centerContent = {
                    Text(
                        "Add note",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Title")
            TextField(value = viewModel.title, onValueChange = { viewModel.title = it })

            Text("Place")
            TextField(value = viewModel.place, onValueChange = { viewModel.place = it })

            Text("Date")
            TextField(value = displayDate(viewModel.dueDate), onValueChange = { viewModel.dueDate = it.toLong()})
            Button(onClick = { viewModel.showDatePicker = true }) {
                Text("Select Date")
                DatePickerDialogCustom(show = viewModel.showDatePicker,
                    onDismiss = { viewModel.showDatePicker = false },
                    initialSelectedDateMillis = viewModel.dueDate,
                    onDateSelected = { timestamp ->
                        viewModel.dueDate = timestamp
                    })
            }

            Text("Time")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Starts: ")
                        Text(
                            text = displayTime(viewModel.startTime),
                            modifier = Modifier.clickable { viewModel.showStartTimePicker = true })
                        TimePickerDialogCustom(
                            viewModel.showStartTimePicker,
                            { viewModel.showStartTimePicker = false },
                            viewModel.startTime,
                            false,
                            "Starting Time",
                            { timestamp ->
                                viewModel.startTime = timestamp
                            })

                    }
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ends: ")
                        Text(
                            text = displayTime(viewModel.endTime),
                            modifier = Modifier.clickable { viewModel.showEndTimePicker = true })
                        TimePickerDialogCustom(
                            viewModel.showEndTimePicker,
                            { viewModel.showEndTimePicker = false },
                            viewModel.endTime,
                            false,
                            "Ending Time",
                            { timestamp ->
                                viewModel.endTime = timestamp
                            })

                    }
                }

            }

            Text("Notes")
            TextField(value = viewModel.noteContent, onValueChange = { viewModel.noteContent = it })

            ExposedDropdownMenuBox(
                expanded = viewModel.priorityExpanded,
                onExpandedChange = { viewModel.priorityExpanded = !viewModel.priorityExpanded }
            ) {
                TextField(
                    value = viewModel.priority,
                    onValueChange = { viewModel.priority = it },
                    readOnly = true,
                    label = { Text("Priority") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(viewModel.priorityExpanded) },
                    modifier = Modifier
                        .menuAnchor(PrimaryEditable, true)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = viewModel.priorityExpanded,
                    onDismissRequest = { viewModel.priorityExpanded = false }
                ) {
                    viewModel.priorityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.priority = option
                                viewModel.priorityExpanded = false
                            }
                        )
                    }
                }
            }

            // You can add proper date/time pickers for eventTime later.
            Text("Calendar: ${
                viewModel.calendarTypes.
                collectAsStateWithLifecycle(initialValue = emptyList())
                    .value.firstOrNull()?.name}")
            Text("Alarm: (todo)")

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.onSaveNote()
                    backStack.removeLastOrNull()

                }
            ) {
                Text("Save Note")

            }
        }
    }
}

// Preview example
@Preview
@Composable
fun AddNotesPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = remember { com.arkanzi.today.App.DatabaseProvider.getDatabase(context) }
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    val calendarTypeRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }
    AddNotesScreen(backStack, noteRepository, calendarTypeRepository)
}
