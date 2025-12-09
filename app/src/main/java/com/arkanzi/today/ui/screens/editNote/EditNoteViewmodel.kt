package com.arkanzi.today.ui.screens.editNote

import android.content.Context
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

    // Date + Time
    private val nowMillis = System.currentTimeMillis()

    var startDate by mutableLongStateOf(
        LocalDate.now().atStartOfDay(ZoneOffset.UTC)
            .toInstant().toEpochMilli()
    )
        private set

    var endDate by mutableLongStateOf(nowMillis)
        private set

    var startTime by mutableLongStateOf(nowMillis)
        private set

    var endTime by mutableLongStateOf(nowMillis + 3600000)
        private set

    // Dialogs
    var showStartDatePicker by mutableStateOf(false)
        private set
    var showEndDatePicker by mutableStateOf(false)
        private set
    var showStartTimePicker by mutableStateOf(false)
        private set
    var showEndTimePicker by mutableStateOf(false)
        private set

    // Dropdowns
    var priorityExpanded by mutableStateOf(false)
        private set
    var calendarExpanded by mutableStateOf(false)
        private set

    val priorityOptions = listOf("Low", "Normal", "High")

    // Store original note info
    private var originalNoteId: Long? = null
    private var originalCreatedAt: Long = 0L

    // ----------------------------------------- //
    //              INITIALIZE NOTE              //
    // ----------------------------------------- //
    fun initializeWithNote(note: Note) {
        originalNoteId = note.id
        originalCreatedAt = note.createdAt

        title = note.title
        place = note.place
        noteContent = note.note
        priority = note.priority
        calendarTypeId = note.calendarTypeId
        isCompleted = note.isCompleted
        isNotificationOn = note.isNotificationOn

        // Set date & time fields
        startDate = note.startDateTime
        endDate = note.endDateTime

        startTime = note.startDateTime
        endTime = note.endDateTime

        // Load calendar type name
        viewModelScope.launch {
            calendarTypes.value.find { it.id == note.calendarTypeId }?.let {
                calendarTypeSelected = it.name
            }
        }
    }

    // ----------------------------------------- //
    //        UPDATE FUNCTIONS (unchanged)       //
    // ----------------------------------------- //
    fun updateTitle(newTitle: String) {
        title = newTitle
        if (titleError && newTitle.isNotBlank()) titleError = false
    }

    fun updatePlace(newPlace: String) { place = newPlace }

    fun updateNoteContent(newContent: String) { noteContent = newContent }

    fun updatePriority(newPriority: String) {
        priority = newPriority
        priorityExpanded = false
    }

    fun updateCalendarType(type: String, id: Long) {
        calendarTypeSelected = type
        calendarTypeId = id
        calendarExpanded = false
    }

    fun toggleIsNotificationOn() {
        isNotificationOn = !isNotificationOn
    }

    fun updateStartDate(v: Long) { startDate = v; showStartDatePicker = false }
    fun updateEndDate(v: Long) { endDate = v; showEndDatePicker = false }
    fun updateStartTime(v: Long) { startTime = v; showStartTimePicker = false }
    fun updateEndTime(v: Long) { endTime = v; showEndTimePicker = false }

    fun showStartDatePicker() { showStartDatePicker = true }
    fun hideStartDatePicker() { showStartDatePicker = false }
    fun showEndDatePicker() { showEndDatePicker = true }
    fun hideEndDatePicker() { showEndDatePicker = false }
    fun showStartTimePicker() { showStartTimePicker = true }
    fun hideStartTimePicker() { showStartTimePicker = false }
    fun showEndTimePicker() { showEndTimePicker = true }
    fun hideEndTimePicker() { showEndTimePicker = false }

    fun togglePriorityDropdown() { priorityExpanded = !priorityExpanded }
    fun toggleCalendarDropdown() { calendarExpanded = !calendarExpanded }

    // ----------------------------------------- //
    //               UPDATE NOTE                 //
    // ----------------------------------------- //
    fun onUpdateNote(context: Context, onSuccess: () -> Unit = {}) {
        if (title.isBlank()) {
            titleError = true
            return
        }

        originalNoteId?.let { id ->
            val updatedNote = Note(
                id = id,
                title = title,
                place = place,
                startDateTime = combineDateAndTime(startDate, startTime),
                endDateTime = combineDateAndTime(endDate, endTime),
                note = noteContent,
                priority = priority,
                calendarTypeId = calendarTypeId,
                createdAt = originalCreatedAt,
                isCompleted = isCompleted,
                isNotificationOn = isNotificationOn
            )

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    noteRepository.update(updatedNote)

                    if (isNotificationOn) {
                        ReminderScheduler.scheduleReminder(
                            context,
                            updatedNote.id.toInt(),
                            updatedNote.startDateTime,
                            updatedNote.title,
                            "You have a reminder."
                        )
                    } else {
                        ReminderScheduler.cancelReminder(context, updatedNote.id.toInt())
                    }

                    withContext(Dispatchers.Main) { onSuccess() }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) { titleError = true }
                }
            }
        }
    }

}
