package com.arkanzi.today.ui.screens.addNote

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.CalendarType
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.util.combineDateAndTime
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar

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
    init {
        viewModelScope.launch {
            calendarTypes.collect { types ->
                if (types.isNotEmpty()) {
                    calendarTypeSelected = types.first().name
                    calendarTypeId = types.first().id
                }
            }
        }
    }


    var title by mutableStateOf("")
    var place by mutableStateOf("")
    var startDate by mutableLongStateOf(LocalDate.now().atStartOfDay(ZoneOffset.UTC)
        .toInstant().toEpochMilli())
    var endDate by mutableLongStateOf(System.currentTimeMillis())
    var startTime by mutableLongStateOf(System.currentTimeMillis())
    var endTime by mutableLongStateOf(System.currentTimeMillis())
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




    fun onSaveNote() {

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
        viewModelScope.launch {
            noteRepository.insert(newNote)

        }
    }
}
