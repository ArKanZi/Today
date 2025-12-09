package com.arkanzi.today.ui.screens.search

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

class SearchViewModel (private val noteRepository: NoteRepository): ViewModel() {

    private var _noteToDelete: Note? = null

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _query = MutableStateFlow("")

    private val _deletingNoteIds = MutableStateFlow<Set<Long>>(emptySet())
    val deletingNoteIds: StateFlow<Set<Long>> = _deletingNoteIds.asStateFlow()

    private val _results = MutableStateFlow<List<Note>>(emptyList())
    val results: StateFlow<List<Note>> = _results.asStateFlow()

    private val _upcomingNotes = MutableStateFlow<List<Note>>(emptyList())
    val upcomingNotes: StateFlow<List<Note>> = _upcomingNotes.asStateFlow()

    private val _dueNotes = MutableStateFlow<List<Note>>(emptyList())
    val dueNotes: StateFlow<List<Note>> = _dueNotes.asStateFlow()

    private val _historyNotes = MutableStateFlow<List<Note>>(emptyList())
    val historyNotes: StateFlow<List<Note>> = _historyNotes.asStateFlow()



    fun updateQuery(newQuery: String, currentTime: Long) {
        _query.value = newQuery
        performSearch(newQuery, currentTime)
    }

    fun toggleCompleted(note: Note) {
        val updatedNote = note.copy(isCompleted = !note.isCompleted)
        viewModelScope.launch {
            noteRepository.update(updatedNote)
        }
    }

    fun showDeleteConfirmation(note: Note) {
        _noteToDelete = note
        _showDeleteDialog.value = true
        closeNoteOptions() // Close swipe options when dialog opens
    }
    private val _expandedNoteId = MutableStateFlow<Long?>(null)
    val expandedNoteId: StateFlow<Long?> = _expandedNoteId.asStateFlow()

    fun closeNoteOptions() {
        _expandedNoteId.value = null
    }

    private var searchJob: Job? = null

    private fun performSearch(query: String, currentTime: Long) {
        // Stop previous search so only the latest one runs
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            if (query.isBlank()) {
                _results.value = emptyList()
                _upcomingNotes.value = emptyList()
                _dueNotes.value = emptyList()
                _historyNotes.value = emptyList()
                return@launch
            }

            noteRepository.getSearchNotes(query).collect { notes ->
                val upcoming = mutableListOf<Note>()
                val due = mutableListOf<Note>()
                val history = mutableListOf<Note>()

                for (note in notes) {
                    when {
                        note.isCompleted -> history += note
                        note.endDateTime < currentTime -> due += note
                        note.endDateTime >= currentTime -> upcoming += note
                    }
                }

                _upcomingNotes.value = upcoming
                _dueNotes.value = due
                _historyNotes.value = history
            }
        }
    }



    private val _expandedSections = MutableStateFlow(setOf("upcoming","due","history","result"))
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

    fun onDeleteConfirm() {
        _noteToDelete?.let { note ->
            // Add to deleting set for animation
            _deletingNoteIds.value += note.id

            viewModelScope.launch {
                // Delay to allow animation to complete
                delay(500) // Match animation duration

                // Actually delete from database
                noteRepository.delete(note)

                // Remove from deleting set
                _deletingNoteIds.value -= note.id
            }
        }
        onDeleteDismiss()
    }

    fun onDeleteDismiss() {
        _showDeleteDialog.value = false
        _noteToDelete = null
    }

}