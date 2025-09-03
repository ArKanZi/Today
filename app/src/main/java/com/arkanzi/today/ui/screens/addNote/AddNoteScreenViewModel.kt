package com.arkanzi.today.ui.screens.addNote

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.CalendarType
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val noteRepository: NoteRepository,
    calendarTypeRepository: CalendarTypeRepository
) : ViewModel() {

    val calendarTypes: StateFlow<List<CalendarType>> =
        calendarTypeRepository.allCalendarType.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    var title by mutableStateOf("")
    var place by mutableStateOf("")
    var dueDate by mutableLongStateOf(System.currentTimeMillis())
    var startTime by mutableLongStateOf(System.currentTimeMillis())
    var endTime by mutableLongStateOf(System.currentTimeMillis())
    var noteContent by mutableStateOf("")
    var priority by mutableStateOf("Normal")
    var calendarTypeId by mutableLongStateOf(0)
    var isCompleted by mutableStateOf(false)

    var showDatePicker by mutableStateOf(false)

    var showStartTimePicker by mutableStateOf(false)
    var showEndTimePicker by mutableStateOf(false)

    val priorityOptions = listOf("Low", "Normal", "High")
    var priorityExpanded by mutableStateOf(false)



    fun onSaveNote() {

        calendarTypeId = calendarTypes.value.firstOrNull()?.id!!

        val newNote = Note(
            title = title,
            place = place,
            dueDate = dueDate,
            startTime = startTime,
            endTime = endTime,
            note = noteContent,
            priority = priority,
            calendarTypeId = calendarTypeId,
            createdAt = System.currentTimeMillis(),
            isCompleted = isCompleted
        )
        viewModelScope.launch {
            noteRepository.insert(newNote)

        }
    }
}
