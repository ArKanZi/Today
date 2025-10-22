package com.arkanzi.today.ui.screens.viewAllNotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.NoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewAllNotesViewmodel(
    private val noteRepository: NoteRepository,
    private val notesType: String
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _screenTitle = MutableStateFlow("")
    val screenTitle: StateFlow<String> = _screenTitle.asStateFlow()

    private val _deletingNoteIds = MutableStateFlow<Set<Long>>(emptySet())
    val deletingNoteIds: StateFlow<Set<Long>> = _deletingNoteIds.asStateFlow()

    private val _expandedNoteId = MutableStateFlow<Long?>(null)
    val expandedNoteId: StateFlow<Long?> = _expandedNoteId.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private var _noteToDelete: Note? = null
    private var notesJob: Job? = null

    init {
        setScreenTitle()
    }

    private fun setScreenTitle() {
        _screenTitle.value = when (notesType.lowercase()) {
            "upcoming" -> "Upcoming Notes"
            "due" -> "Due Notes"
            "history" -> "History Notes"
            else -> "All Notes"
        }
    }

    fun loadNotes() {
        if (notesJob?.isActive == true) return // Prevent multiple calls

        notesJob = viewModelScope.launch {
            _isLoading.value = true
            try {
                val notesFlow = when (notesType.lowercase()) {
                    "upcoming" -> noteRepository.getAllUpcoming()
                    "due" -> noteRepository.getAllDue()
                    "history" -> noteRepository.getAllHistory()
                    else -> noteRepository.allNotes
                }

                notesFlow.collect { notesList ->
                    _notes.value = notesList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error
            }
        }
    }

    fun stopLoading() {
        notesJob?.cancel()
        notesJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopLoading()
    }

    fun toggleCompleted(note: Note) {
        val updatedNote = note.copy(isCompleted = !note.isCompleted)
        viewModelScope.launch {
            noteRepository.update(updatedNote)
        }
    }

    fun toggleNoteOptions(noteId: Long) {
        _expandedNoteId.value = if (_expandedNoteId.value == noteId) null else noteId
    }

    fun closeNoteOptions() {
        _expandedNoteId.value = null
    }

    fun showDeleteConfirmation(note: Note) {
        _noteToDelete = note
        _showDeleteDialog.value = true
        closeNoteOptions()
    }

    fun onDeleteConfirm() {
        _noteToDelete?.let { note ->
            _deletingNoteIds.value = _deletingNoteIds.value + note.id

            viewModelScope.launch {
                delay(500) // Animation delay
                noteRepository.delete(note)
                _deletingNoteIds.value = _deletingNoteIds.value - note.id
            }
        }
        onDeleteDismiss()
    }

    fun onDeleteDismiss() {
        _showDeleteDialog.value = false
        _noteToDelete = null
    }
}
