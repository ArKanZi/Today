package com.arkanzi.today.ui.screens.addNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository

class AddNoteViewModelFactory(
    private val noteRepository: NoteRepository,private val calendarTypeRepository: CalendarTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNoteViewModel(noteRepository,calendarTypeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
