package com.arkanzi.today.ui.screens.viewAllNotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkanzi.today.repository.NoteRepository

class ViewAllNotesViewmodelFactory(
        private val noteRepository: NoteRepository,
    private val notesType: String,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ViewAllNotesViewmodel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ViewAllNotesViewmodel(noteRepository,notesType) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }