package com.arkanzi.today.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewmodel(private val noteRepository: NoteRepository): ViewModel() {
    val notes: StateFlow<List<Note>> = noteRepository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val upcomingNotesCount: StateFlow<Int> = noteRepository.getTotalUpcomingCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0)

    val upcoming6Notes: StateFlow<List<Note>> = noteRepository.get6Upcoming()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val due6Notes: StateFlow<List<Note>> = noteRepository.get6Due()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())



    val history6Notes: StateFlow<List<Note>> = noteRepository.get6History()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _expandedNoteId = MutableStateFlow<Long?>(null)
    val expandedNoteId: StateFlow<Long?> = _expandedNoteId.asStateFlow()

    private val _deletingNoteIds = MutableStateFlow<Set<Long>>(emptySet())
    val deletingNoteIds: StateFlow<Set<Long>> = _deletingNoteIds.asStateFlow()


    private var _noteToDelete: Note? = null


    fun toggleCompleted(note: Note) {
        val updatedNote = note.copy(isCompleted = !note.isCompleted)
        viewModelScope.launch {
            noteRepository.update(updatedNote)
    }
    }

    // Handle swipe reveal state
    fun toggleNoteOptions(noteId: Long) {
        _expandedNoteId.value = if (_expandedNoteId.value == noteId) null else noteId
    }

    fun closeNoteOptions() {
        _expandedNoteId.value = null
    }

    fun showDeleteConfirmation(note: Note) {
        _noteToDelete = note
        _showDeleteDialog.value = true
        closeNoteOptions() // Close swipe options when dialog opens
    }

    fun onDeleteConfirm() {
        _noteToDelete?.let { note ->
            // Add to deleting set for animation
            _deletingNoteIds.value = _deletingNoteIds.value + note.id

            viewModelScope.launch {
                // Delay to allow animation to complete
                delay(500) // Match animation duration

                // Actually delete from database
                noteRepository.delete(note)

                // Remove from deleting set
                _deletingNoteIds.value = _deletingNoteIds.value - note.id
            }
        }
        onDeleteDismiss()
    }

    fun onDeleteDismiss() {
        _showDeleteDialog.value = false
        _noteToDelete = null
    }

    private val _expandedSections = MutableStateFlow<Set<String>>(emptySet())
    val expandedSections: StateFlow<Set<String>> = _expandedSections.asStateFlow()

    fun toggleSectionExpanded(sectionName: String) {
        val currentExpanded = _expandedSections.value.toMutableSet()
        if (currentExpanded.contains(sectionName)) {
            currentExpanded.remove(sectionName)
        } else {
            currentExpanded.add(sectionName)
        }
        _expandedSections.value = currentExpanded
    }
}