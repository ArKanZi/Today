package com.arkanzi.today.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainViewmodel(
    private val noteRepository: NoteRepository
) : ViewModel() {

    // Splash loading state
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch(Dispatchers.IO) {
            // 2️⃣ Preload all note queries
            noteRepository.allNotes.firstOrNull()
            noteRepository.get6Upcoming().firstOrNull()
            noteRepository.get6Due().firstOrNull()
            noteRepository.get6History().firstOrNull()
            noteRepository.getTotalUpcomingCount().firstOrNull()

            _isLoading.value = false
        }
    }

    // ----------------------------------------------------
    // Your existing flows (unchanged)
    // ----------------------------------------------------

    val notes: StateFlow<List<Note>> = noteRepository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val upcomingNotesCount: StateFlow<Int> = noteRepository.getTotalUpcomingCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), 0)

    val upcoming6Notes = noteRepository.get6Upcoming()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val due6Notes = noteRepository.get6Due()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val history6Notes = noteRepository.get6History()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    // ----------------------------------------------------
    // Your UI-related logic (unchanged)
    // ----------------------------------------------------

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _expandedNoteId = MutableStateFlow<Long?>(null)
    val expandedNoteId: StateFlow<Long?> = _expandedNoteId.asStateFlow()

    private val _deletingNoteIds = MutableStateFlow<Set<Long>>(emptySet())
    val deletingNoteIds: StateFlow<Set<Long>> = _deletingNoteIds.asStateFlow()

    private var _noteToDelete: Note? = null

    fun toggleCompleted(note: Note) {
        val updated = note.copy(isCompleted = !note.isCompleted)
        viewModelScope.launch { noteRepository.update(updated) }
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
            _deletingNoteIds.value += note.id

            viewModelScope.launch {
                delay(500)
                noteRepository.delete(note)
                _deletingNoteIds.value -= note.id
            }
        }
        onDeleteDismiss()
    }

    fun onDeleteDismiss() {
        _showDeleteDialog.value = false
        _noteToDelete = null
    }

    private val _expandedSections = MutableStateFlow<Set<String>>(setOf("upcoming"))
    val expandedSections: StateFlow<Set<String>> = _expandedSections.asStateFlow()

    fun toggleSectionExpanded(sectionName: String) {
        val updated = _expandedSections.value.toMutableSet()
        if (updated.contains(sectionName)) updated.remove(sectionName)
        else updated.add(sectionName)
        _expandedSections.value = updated
    }
}
