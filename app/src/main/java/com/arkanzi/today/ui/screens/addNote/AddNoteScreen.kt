package com.arkanzi.today.ui.screens.addNote

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.arkanzi.today.App
import com.arkanzi.today.R
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.DatePickerDialogCustom
import com.arkanzi.today.ui.components.IconContainer
import com.arkanzi.today.ui.components.InputFields
import com.arkanzi.today.ui.components.TimePickerDialogCustom
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.util.CustomSelectableDates
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
                            tint = MaterialTheme.colorScheme.onSurface,
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
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
//            Title
            InputFields(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                icon = R.drawable.ic_typography,
                description = "",
                label = "Title",
                tint = Color(0xFF3DB2C5),

                )
//            Place
            InputFields(
                value = viewModel.place,
                onValueChange = { viewModel.place = it },
                icon = R.drawable.ic_location,
                description = "",
                label = "Place",
                tint = Color(0xFFdb7ccb),

                )
//            Starting Date and Time
            Row(modifier = Modifier.fillMaxWidth()) {
                Box {
                    IconContainer(
                        icon = R.drawable.ic_clock,
                        description = "",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color(0xFF64a4cc),
                        onClick = { }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    InputFields(
                        modifier = Modifier
                            .weight(2f)
                            .clickable { viewModel.showStartDatePicker = true },
                        value = displayDate(viewModel.startDate),
                        onValueChange = { viewModel.startDate = it.toLong() },
                        isIcon = false,
                        isEnabled = false,
                        label = "Starting Date and Time",
                        tint = Color.Gray,
                        singleLine = true,
                    )
                    DatePickerDialogCustom(
                        show = viewModel.showStartDatePicker,
                        onDismiss = { viewModel.showStartDatePicker = false },
                        initialSelectedDateMillis = viewModel.startDate,
                        onDateSelected = { timestamp ->
                            viewModel.startDate = timestamp
                        })
                    InputFields(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.showStartTimePicker = true },
                        value = displayTime(viewModel.startTime),
                        onValueChange = { viewModel.startTime = it.toLong() },
                        isEnabled = false,
                        isIcon = false,
                        label = "",
                        tint = Color.Gray,
                        singleLine = true,
                    )
                    TimePickerDialogCustom(
                        viewModel.showStartTimePicker,
                        { viewModel.showStartTimePicker = false },
                        viewModel.startTime,
                        false,
                        "Starting Time",
                        { timestamp ->
                            viewModel.startTime = timestamp
                            Log.d("AddNoteScreen", "Selected start time: $timestamp")
                        })
                }

            }
//            Ending Date and Time
            Row(modifier = Modifier.fillMaxWidth()) {
                Box {
                    IconContainer(
                        icon = R.drawable.ic_clock,
                        description = "",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color(0xFFD73154),
                        onClick = { }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    InputFields(
                        modifier = Modifier
                            .weight(2f)
                            .clickable { viewModel.showEndDatePicker = true },
                        value = displayDate(viewModel.endDate),
                        onValueChange = { viewModel.endDate = it.toLong() },
                        isEnabled = false,
                        isIcon = false,
                        label = "Ending Date and Time",
                        tint = Color.Gray,
                        singleLine = true,
                    )
                    DatePickerDialogCustom(
                        show = viewModel.showEndDatePicker,
                        onDismiss = { viewModel.showEndDatePicker = false },
                        initialSelectedDateMillis = viewModel.startDate,
                        selectableDates = CustomSelectableDates(viewModel.startDate),
                        onDateSelected = { timestamp ->
                            viewModel.endDate = timestamp
                        })
                    InputFields(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.showEndTimePicker = true },
                        value = displayTime(viewModel.endTime),
                        onValueChange = { viewModel.endTime = it.toLong() },
                        isEnabled = false,
                        isIcon = false,
                        label = "",
                        tint = Color.Gray,
                        singleLine = true,
                    )
                    TimePickerDialogCustom(
                        viewModel.showEndTimePicker,
                        { viewModel.showEndTimePicker = false },
                        viewModel.endTime,
                        false,
                        "Starting Time",
                        { timestamp ->
                            viewModel.endTime = timestamp
                            Log.d("AddNoteScreen", "Selected end time: $timestamp")
                        })

                }
            }
//            Notes
            InputFields(
                value = viewModel.noteContent,
                onValueChange = { viewModel.noteContent = it },
                icon = R.drawable.ic_notes,
                description = "",
                label = "Notes",
                tint = Color(0xFFE88196),

                )
//            Priority
            Row {
                IconContainer(
                    icon = R.drawable.ic_flag,
                    description = "",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color(0xFF6abcc4),
                    onClick = {}
                )
                ExposedDropdownMenuBox(
                    expanded = viewModel.priorityExpanded,
                    onExpandedChange = { viewModel.priorityExpanded = !viewModel.priorityExpanded }
                ) {
                    InputFields(
                        modifier = Modifier
                            .weight(1f)
                            .menuAnchor(PrimaryNotEditable, true),
                        value = viewModel.priority,
                        onValueChange = { viewModel.priority = it },
                        readOnly = true,
                        isIcon = false,
                        label = "Priority",
                        tint = Color.Gray,
                        singleLine = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(viewModel.priorityExpanded) }
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
            }
//            Calendar Type
            Row {
                IconContainer(
                    icon = R.drawable.ic_calendar,
                    description = "",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color(0xFF988ad3),
                    onClick = {}
                )
                ExposedDropdownMenuBox(
                    expanded = viewModel.calendarExpanded,
                    onExpandedChange = { viewModel.calendarExpanded = !viewModel.calendarExpanded }
                ) {
                    InputFields(
                        modifier = Modifier
                            .weight(1f)
                            .menuAnchor(PrimaryNotEditable, true),
                        value = viewModel.calendarTypeSelected,
                        onValueChange = { viewModel.calendarTypeSelected = it },
                        readOnly = true,
                        isIcon = false,
                        label = "Calendar Type",
                        tint = Color.Gray,
                        singleLine = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(viewModel.priorityExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = viewModel.calendarExpanded,
                        onDismissRequest = { viewModel.calendarExpanded = false }
                    ) {
                        viewModel.calendarTypes.collectAsStateWithLifecycle(initialValue = emptyList())
                            .value.forEach { calenderType ->
                                DropdownMenuItem(
                                    text = { Text(calenderType.name) },
                                    onClick = {
                                        viewModel.calendarTypeId = calenderType.id
                                        viewModel.calendarExpanded = false
                                    }
                                )
                            }
                    }
                }
            }
//            Alarm
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically)
            {
                IconContainer(
                    icon = R.drawable.ic_notification,
                    description = "",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color(0xFF2FE16A),
                    onClick = {}
                )
                Column(modifier = Modifier.weight(3f)) { Text("Notification") }
                Column(modifier = Modifier.weight(1f)) {
                    Switch(
                        checked = viewModel.isAlarmSet,
                        onCheckedChange = { viewModel.isAlarmSet = it },
                        thumbContent = if (viewModel.isAlarmSet) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        })
                }
            }

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

@Preview
@Composable
fun AddNotesPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = remember { App.DatabaseProvider.getDatabase(context) }
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    val calendarTypeRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }
    AddNotesScreen(backStack, noteRepository, calendarTypeRepository)
}
