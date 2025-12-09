package com.arkanzi.today.ui.screens.noteDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.arkanzi.today.App
import com.arkanzi.today.R
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.ui.components.IconContainer
import com.arkanzi.today.ui.components.InputFields
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.util.displayDate
import com.arkanzi.today.util.displayTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    backStack: NavBackStack<NavKey>,
    calendarTypeRepository: CalendarTypeRepository,
    note: Note
) {
    val viewModel: NoteDetailViewmodel = viewModel(
        factory = NoteDetailViewmodelFactory(calendarTypeRepository)
    )
    val calendarName by viewModel.calendarName.collectAsState()

    LaunchedEffect(note.calendarTypeId) {
        viewModel.loadCalendarName(note.calendarTypeId)
    }
    val scope = rememberCoroutineScope()
    DefaultLayout(topBar = {
        TopAppBarCustom(
            leftContent = {
                IconButton(
                    onClick = {
                        scope.launch {
                            backStack.removeLastOrNull()
                        }
                    },
                    modifier = Modifier
                        .size(24.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cross),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)

                    )
                }
            },
            centerContent = {
                Text(
                    "Details",
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
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
//            Title
            BasicText(
                text = note.title,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    maxFontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    stepSize = 1.sp
                ),
                style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 2
            )
//            Place
            InputFields(
                value = note.place,
                onValueChange = {},
                textFieldModifier = Modifier,
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
                InputFields(
                    modifier = Modifier.weight(2f),
                    textFieldModifier = Modifier,
                    value = displayDate(note.startDateTime),
                    onValueChange = {},
                    isIcon = false,
                    isEnabled = false,
                    label = "Starting Date and Time",
                    tint = Color.Gray,
                    singleLine = true,
                )
                InputFields(
                    modifier = Modifier.weight(1.1f),
                    textFieldModifier = Modifier,
                    value = displayTime(note.startDateTime),
                    onValueChange = { },
                    isEnabled = false,
                    isIcon = false,
                    label = "",
                    tint = Color.Gray,
                    singleLine = true,
                )

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

                InputFields(
                    modifier = Modifier.weight(2f),
                    textFieldModifier = Modifier,
                    value = displayDate(note.endDateTime),
                    onValueChange = { },
                    isEnabled = false,
                    isIcon = false,
                    label = "Ending Date and Time",
                    tint = Color.Gray,
                    singleLine = true,
                )
                InputFields(
                    modifier = Modifier.weight(1.1f),
                    textFieldModifier = Modifier,
                    value = displayTime(note.endDateTime),
                    onValueChange = {},
                    isEnabled = false,
                    isIcon = false,
                    label = "",
                    tint = Color.Gray,
                    singleLine = true,
                )
            }
//            Notes
            InputFields(
                textFieldModifier = Modifier,
                value = note.note,
                onValueChange = { },
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

                InputFields(
                    modifier = Modifier.weight(1f),
                    textFieldModifier = Modifier,
                    value = note.priority,
                    onValueChange = { },
                    readOnly = true,
                    isIcon = false,
                    label = "Priority",
                    tint = Color.Gray,
                    singleLine = true,
                )
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
                InputFields(
                    modifier = Modifier.weight(1f),
                    textFieldModifier = Modifier,
                    value = calendarName,
                    onValueChange = { },
                    readOnly = true,
                    isIcon = false,
                    label = "Calendar",
                    tint = Color.Gray,
                    singleLine = true,
                )
            }
//            Notification
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
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
                        checked = note.isNotificationOn,
                        onCheckedChange = {},
                        enabled = false,
                        thumbContent = if (note.isNotificationOn) {
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
        }

    }
}

@Preview
@Composable
fun NoteDetailScreenPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = remember { App.DatabaseProvider.getDatabase(context) }
    val calendarRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }
    val note = Note(
        id = 1,
        title = "Meeting with Team",
        place = "Office",
        startDateTime = 1672531200000, // Example timestamp
        endDateTime = 1672639200000,   // Example timestamp
        note = "Discuss project milestones and deadlines.",
        priority = "High",
        calendarTypeId = 0,
        createdAt = 1672444800000, // Example timestamp
    )
    NoteDetailScreen(backStack, calendarRepository, note)
}