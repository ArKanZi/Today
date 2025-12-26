package com.arkanzi.today.ui.screens.addNote

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
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
    backStack: NavBackStack<NavKey>,
    noteRepository: NoteRepository,
    calendarTypeRepository: CalendarTypeRepository
) {
    val context = LocalContext.current
    val viewModel: AddNoteViewModel = viewModel(
        factory = AddNoteViewModelFactory(noteRepository, calendarTypeRepository)
    )
    val calendarTypes by viewModel.calendarTypes.collectAsState()

    LaunchedEffect(calendarTypes) {
        if (calendarTypes.isNotEmpty()) {
            viewModel.updateCalendarType(
                calendarTypes.first().name,
                calendarTypes.first().id
            )
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Title
        InputFields(
            value = viewModel.title,
            onValueChange = viewModel::updateTitle, // Use ViewModel function
            icon = R.drawable.ic_typography,
            description = "",
            label = "Title",
            tint = Color(0xFF3DB2C5),
            singleLine = true,
            maxLength = 50,
            isError = viewModel.titleError,
            trailingIcon = if (viewModel.titleError) {
                {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_error),
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else null
        )

        // Place
        InputFields(
            value = viewModel.place,
            onValueChange = viewModel::updatePlace, // Use ViewModel function
            icon = R.drawable.ic_location,
            description = "",
            label = "Place",
            tint = Color(0xFFdb7ccb),
            singleLine = true,
            maxLength = 50
        )

        // Starting Date and Time
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
                        .clickable { viewModel.showStartDatePicker() }, // Use ViewModel function
                    value = displayDate(viewModel.startDate),
                    onValueChange = { }, // No direct change needed
                    isIcon = false,
                    isEnabled = false,
                    label = "Starting Date and Time",
                    tint = Color.Gray,
                    singleLine = true,
                )
                DatePickerDialogCustom(
                    show = viewModel.showStartDatePicker,
                    onDismiss = viewModel::hideStartDatePicker, // Use ViewModel function
                    initialSelectedDateMillis = viewModel.startDate,
                    onDateSelected = viewModel::updateStartDate // Use ViewModel function
                )
                InputFields(
                    modifier = Modifier
                        .weight(1.1f)
                        .clickable { viewModel.showStartTimePicker() }, // Use ViewModel function
                    value = displayTime(viewModel.startTime),
                    onValueChange = { }, // No direct change needed
                    isEnabled = false,
                    isIcon = false,
                    label = "",
                    tint = Color.Gray,
                    singleLine = true,
                )
                TimePickerDialogCustom(
                    viewModel.showStartTimePicker,
                    viewModel::hideStartTimePicker, // Use ViewModel function
                    viewModel.startTime,
                    false,
                    "Starting Time"
                ) { timestamp ->
                    viewModel.updateStartTime(timestamp) // Use ViewModel function
                    Log.d("AddNoteScreen", "Selected start time: $timestamp")
                }
            }
        }

        // Ending Date and Time
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
                        .clickable { viewModel.showEndDatePicker() }, // Use ViewModel function
                    value = displayDate(viewModel.endDate),
                    onValueChange = { }, // No direct change needed
                    isEnabled = false,
                    isIcon = false,
                    label = "Ending Date and Time",
                    tint = Color.Gray,
                    singleLine = true,
                )
                DatePickerDialogCustom(
                    show = viewModel.showEndDatePicker,
                    onDismiss = viewModel::hideEndDatePicker, // Use ViewModel function
                    initialSelectedDateMillis = viewModel.startDate,
                    selectableDates = CustomSelectableDates(viewModel.startDate),
                    onDateSelected = viewModel::updateEndDate // Use ViewModel function
                )
                InputFields(
                    modifier = Modifier
                        .weight(1.1f)
                        .clickable { viewModel.showEndTimePicker() }, // Use ViewModel function
                    value = displayTime(viewModel.endTime),
                    onValueChange = { }, // No direct change needed
                    isEnabled = false,
                    isIcon = false,
                    label = "",
                    tint = Color.Gray,
                    singleLine = true,
                )
                TimePickerDialogCustom(
                    viewModel.showEndTimePicker,
                    viewModel::hideEndTimePicker, // Use ViewModel function
                    viewModel.endTime,
                    false,
                    "Ending Time"
                ) { timestamp ->
                    viewModel.updateEndTime(timestamp) // Use ViewModel function
                    Log.d("AddNoteScreen", "Selected end time: $timestamp")
                }
            }
        }

        // Notes
        InputFields(
            value = viewModel.noteContent,
            onValueChange = viewModel::updateNoteContent, // Use ViewModel function
            icon = R.drawable.ic_notes,
            description = "",
            label = "Notes",
            tint = Color(0xFFE88196),
            maxLines = 50,
            maxLength = 150
        )

        // Priority
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
                onExpandedChange = { viewModel.togglePriorityDropdown() } // Use ViewModel function
            ) {
                InputFields(
                    modifier = Modifier
                        .weight(1f)
                        .menuAnchor(PrimaryNotEditable, true),
                    value = viewModel.priority,
                    onValueChange = { }, // No direct change needed
                    readOnly = true,
                    isIcon = false,
                    label = "Priority",
                    tint = Color.Gray,
                    singleLine = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(viewModel.priorityExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = viewModel.priorityExpanded,
                    onDismissRequest = { viewModel.togglePriorityDropdown() } // Use ViewModel function
                ) {
                    viewModel.priorityOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                viewModel.updatePriority(option) // Use ViewModel function
                            }
                        )
                    }
                }
            }
        }

        // Calendar Type
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
                onExpandedChange = { viewModel.toggleCalendarDropdown() } // Use ViewModel function
            ) {
                InputFields(
                    modifier = Modifier
                        .weight(1f)
                        .menuAnchor(PrimaryNotEditable, true),
                    value = viewModel.calendarTypeSelected,
                    onValueChange = { }, // No direct change needed
                    readOnly = true,
                    isIcon = false,
                    label = "Calendar Type",
                    tint = Color.Gray,
                    singleLine = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(viewModel.calendarExpanded) } // Fixed typo
                )
                ExposedDropdownMenu(
                    expanded = viewModel.calendarExpanded,
                    onDismissRequest = { viewModel.toggleCalendarDropdown() } // Use ViewModel function
                ) {
                    calendarTypes.forEach { calendarType -> // Use direct state instead of collecting again
                        DropdownMenuItem(
                            text = { Text(calendarType.name) },
                            onClick = {
                                viewModel.updateCalendarType(
                                    calendarType.name,
                                    calendarType.id
                                ) // Use ViewModel function
                            }
                        )
                    }
                }
            }
        }

        // Notification
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconContainer(
                icon = R.drawable.ic_notification,
                description = "",
                modifier = Modifier.padding(end = 8.dp),
                tint = Color(0xFF2FE16A),
                onClick = {}
            )
            Column(modifier = Modifier.weight(3f)) {
                Text("Notification")
            }
            Column(modifier = Modifier.weight(1f)) {
                Switch(
                    checked = viewModel.isNotificationOn,
                    onCheckedChange = { viewModel.toggleIsNotificationOn() }, // Use ViewModel function
                    thumbContent = if (viewModel.isNotificationOn) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }

        // Save Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 24.dp)
                .shadow(
                    1.dp,
                    shape = CircleShape,
                    ambientColor = DefaultShadowColor,
                    spotColor = DefaultShadowColor
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF5CE487),
                            Color(0xFF2EB4C8)
                        )
                    )
                )
                .clickable {
                    viewModel.onSaveNote(context = context) {
                        // Only navigate back if save was successful (no title error)
                        if (!viewModel.titleError) {
                            backStack.removeLastOrNull()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Save",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Save",
                    tint = MaterialTheme.colorScheme.surfaceContainerLowest,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun AddNotesPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = remember { App.DatabaseProvider.getDatabase(context) }
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    val calendarTypeRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }
    AddNotesScreen(backStack, noteRepository, calendarTypeRepository)
}
