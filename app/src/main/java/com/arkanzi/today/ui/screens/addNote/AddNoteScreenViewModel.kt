package com.arkanzi.today.ui.screens.addNote

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

class AddNoteViewModel(
    private val noteRepository: NoteRepository,
    calendarTypeRepository: CalendarTypeRepository
) : ViewModel() {

    val calendarTypes: StateFlow<List<CalendarType>> =
        calendarTypeRepository.allCalendarType.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    var title by mutableStateOf("")
    var titleError by mutableStateOf(false)
    var place by mutableStateOf("")
    var startDate by mutableLongStateOf(
        LocalDate.now().atStartOfDay(ZoneOffset.UTC)
            .toInstant().toEpochMilli()
    )
    private val _currentTime = System.currentTimeMillis()
    var endDate by mutableLongStateOf(_currentTime)
    var startTime by mutableLongStateOf(_currentTime)
    var endTime by mutableLongStateOf(_currentTime + 3600000)
    var noteContent by mutableStateOf("")
    var priority by mutableStateOf("Normal")
    var calendarTypeSelected by mutableStateOf("")
    var calendarTypeId by mutableLongStateOf(0L)
    var isCompleted by mutableStateOf(false)

    var showStartDatePicker by mutableStateOf(false)
    var showEndDatePicker by mutableStateOf(false)

    var showStartTimePicker by mutableStateOf(false)
    var showEndTimePicker by mutableStateOf(false)

    val priorityOptions = listOf("Low", "Normal", "High")
    var priorityExpanded by mutableStateOf(false)

    var calendarExpanded by mutableStateOf(false)

    var isAlarmSet by mutableStateOf(false)


    fun onSaveNote(onSuccess: () -> Unit = {}) {
        if (title.isNotBlank()) {
            val newNote = Note(
                title = title,
                place = place,
                startDateTime = combineDateAndTime(startDate, startTime),
                endDateTime = combineDateAndTime(endDate, endTime),
                note = noteContent,
                priority = priority,
                calendarTypeId = calendarTypeId,
                createdAt = System.currentTimeMillis(),
                isCompleted = isCompleted
            )

            viewModelScope.launch(Dispatchers.IO) { // ← Use IO dispatcher
                try {
                    noteRepository.insert(newNote)
                    withContext(Dispatchers.Main) { // Switch back for UI updates
                        onSuccess() // Callback for navigation
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Handle error if needed
                        titleError = true
                    }
                }
            }
        } else {
            titleError = true
        }
    }
}