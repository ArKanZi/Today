package com.arkanzi.today.ui.screens.addNote

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.CalendarType
import com.arkanzi.today.model.Note
import com.arkanzi.today.notification.ReminderScheduler
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

    // Form fields
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

    var isNotificationOn by mutableStateOf(false)
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

    val priorityOptions = listOf("Low", "Normal", "High")

    // -------- UPDATE FUNCTIONS -------- //
    fun updateTitle(newTitle: String) {
        title = newTitle
        if (titleError && newTitle.isNotBlank()) titleError = false
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

    fun toggleIsNotificationOn() {
        isNotificationOn = !isNotificationOn
    }

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

    fun showStartDatePicker() { showStartDatePicker = true }
    fun hideStartDatePicker() { showStartDatePicker = false }
    fun showEndDatePicker() { showEndDatePicker = true }
    fun hideEndDatePicker() { showEndDatePicker = false }
    fun showStartTimePicker() { showStartTimePicker = true }
    fun hideStartTimePicker() { showStartTimePicker = false }
    fun showEndTimePicker() { showEndTimePicker = true }
    fun hideEndTimePicker() { showEndTimePicker = false }

    fun togglePriorityDropdown() {
        priorityExpanded = !priorityExpanded
    }

    fun toggleCalendarDropdown() {
        calendarExpanded = !calendarExpanded
    }

    // -------------------------------------------------------------- //
    //                           SAVE NOTE                            //
    // -------------------------------------------------------------- //
    fun onSaveNote(context: Context, onSuccess: () -> Unit = {}) {
        if (title.isBlank()) {
            titleError = true
            return
        }

        val startDateTime = combineDateAndTime(startDate, startTime)
        val currentTime = System.currentTimeMillis()

        if (isNotificationOn && startDateTime <= currentTime) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Reminder time must be in the future",
                    Toast.LENGTH_SHORT
                ).show()
            }
            return
        }

        val newNote = Note(
            title = title,
            place = place,
            startDateTime = startDateTime,
            endDateTime = combineDateAndTime(endDate, endTime),
            note = noteContent,
            priority = priority,
            calendarTypeId = calendarTypeId,
            createdAt = currentTime,
            isCompleted = isCompleted,
            isNotificationOn = isNotificationOn
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val noteId = noteRepository.insert(newNote)

                // Schedule only when notification is enabled
                if (isNotificationOn) {
                    ReminderScheduler.scheduleReminder(
                        context,
                        noteId.toInt(),
                        newNote.startDateTime,
                        newNote.title,
                        "You have a reminder."
                    )
                } else {
                    ReminderScheduler.cancelReminder(context, noteId.toInt())
                }

                withContext(Dispatchers.Main) { onSuccess() }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) { titleError = true }
                Log.e("addErrorIssue", e.toString())
            }
        }
    }


}
