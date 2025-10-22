package com.arkanzi.today.ui.screens.editNote

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.CalendarType
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.util.combineDateAndTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneOffset

class EditNoteViewmodel(
    private val noteRepository: NoteRepository,
    calendarTypeRepository: CalendarTypeRepository
) : ViewModel() {

    val calendarTypes: StateFlow<List<CalendarType>> =
        calendarTypeRepository.allCalendarType.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    // Form fields with private setters
    var title by mutableStateOf("")
        private set
    var titleError by mutableStateOf(false)
        private set
    var place by mutableStateOf("")
        private set
    var noteContent by mutableStateOf("")
        private set
    var priority by mutableStateOf("Normal")
        private set
    var calendarTypeSelected by mutableStateOf("")
        private set
    var calendarTypeId by mutableLongStateOf(0L)
        private set
    var isCompleted by mutableStateOf(false)
        private set

    // Date and time fields
    var startDate by mutableLongStateOf(
        LocalDate.now().atStartOfDay(ZoneOffset.UTC)
            .toInstant().toEpochMilli()
    )
        private set

    private val _currentTime = System.currentTimeMillis()
    var endDate by mutableLongStateOf(_currentTime)
        private set
    var startTime by mutableLongStateOf(_currentTime)
        private set
    var endTime by mutableLongStateOf(_currentTime + 3600000)
        private set

    // Dialog states
    var showStartDatePicker by mutableStateOf(false)
        private set
    var showEndDatePicker by mutableStateOf(false)
        private set
    var showStartTimePicker by mutableStateOf(false)
        private set
    var showEndTimePicker by mutableStateOf(false)
        private set

    // Dropdown states
    var priorityExpanded by mutableStateOf(false)
        private set
    var calendarExpanded by mutableStateOf(false)
        private set

    // Other states
    var isAlarmSet by mutableStateOf(false)
        private set

    val priorityOptions = listOf("Low", "Normal", "High")

    private var originalNoteId: Long? = null
    private var originalCreatedAt: Long = 0L

    fun initializeWithNote(note: Note) {
        originalNoteId = note.id
        originalCreatedAt = note.createdAt
        title = note.title
        place = note.place
        noteContent = note.note
        priority = note.priority
        calendarTypeId = note.calendarTypeId
        isCompleted = note.isCompleted

        // Set dates and times
        startDate = note.startDateTime
        endDate = note.endDateTime
        startTime = note.startDateTime
        endTime = note.endDateTime

        // Find and set calendar type name
        viewModelScope.launch {
            calendarTypes.value.find { it.id == note.calendarTypeId }?.let { selectedType ->
                calendarTypeSelected = selectedType.name
            }
        }
    }

    // Update functions for each field
    fun updateTitle(newTitle: String) {
        title = newTitle
        if (titleError && newTitle.isNotBlank()) {
            titleError = false
        }
    }

    fun updatePlace(newPlace: String) {
        place = newPlace
    }

    fun updateNoteContent(newContent: String) {
        noteContent = newContent
    }

    fun updatePriority(newPriority: String) {
        priority = newPriority
        priorityExpanded = false
    }

    fun updateCalendarType(newType: String, typeId: Long) {
        calendarTypeSelected = newType
        calendarTypeId = typeId
        calendarExpanded = false
    }


    fun toggleAlarm() {
        isAlarmSet = !isAlarmSet
    }

    // Date and time update functions
    fun updateStartDate(newDate: Long) {
        startDate = newDate
        showStartDatePicker = false
    }

    fun updateEndDate(newDate: Long) {
        endDate = newDate
        showEndDatePicker = false
    }

    fun updateStartTime(newTime: Long) {
        startTime = newTime
        showStartTimePicker = false
    }

    fun updateEndTime(newTime: Long) {
        endTime = newTime
        showEndTimePicker = false
    }

    // Dialog control functions
    fun showStartDatePicker() {
        showStartDatePicker = true
    }

    fun hideStartDatePicker() {
        showStartDatePicker = false
    }

    fun showEndDatePicker() {
        showEndDatePicker = true
    }

    fun hideEndDatePicker() {
        showEndDatePicker = false
    }

    fun showStartTimePicker() {
        showStartTimePicker = true
    }

    fun hideStartTimePicker() {
        showStartTimePicker = false
    }

    fun showEndTimePicker() {
        showEndTimePicker = true
    }

    fun hideEndTimePicker() {
        showEndTimePicker = false
    }

    // Dropdown control functions
    fun togglePriorityDropdown() {
        priorityExpanded = !priorityExpanded
    }

    fun toggleCalendarDropdown() {
        calendarExpanded = !calendarExpanded
    }

    fun onUpdateNote(onSuccess: () -> Unit = {}) {
        if (title.isNotBlank()) {
            originalNoteId?.let { noteId ->
                val updatedNote = Note(
                    id = noteId,
                    title = title,
                    place = place,
                    startDateTime = combineDateAndTime(startDate, startTime),
                    endDateTime = combineDateAndTime(endDate, endTime),
                    note = noteContent,
                    priority = priority,
                    calendarTypeId = calendarTypeId,
                    createdAt = originalCreatedAt, // Keep original or use existing
                    isCompleted = isCompleted
                )

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        noteRepository.update(updatedNote)
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            titleError = true
                        }
                    }
                }
            }
        } else {
            titleError = true
        }
    }
}
