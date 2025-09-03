package com.arkanzi.today.ui.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainScreenViewModel(private val noteRepository: NoteRepository): ViewModel() {
    val notes: StateFlow<List<Note>> = noteRepository.allNotes
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    var noteExtraOptionsId by mutableStateOf(false)

    fun toggleCompleted(note: Note) {
        val updatedNote = note.copy(isCompleted = !note.isCompleted)
        viewModelScope.launch {
            noteRepository.update(updatedNote)
    }
    }


}