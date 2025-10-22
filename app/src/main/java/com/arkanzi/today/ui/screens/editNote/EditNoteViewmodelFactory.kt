package com.arkanzi.today.ui.screens.editNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository

class EditNoteViewmodelFactory(
    private val noteRepository: NoteRepository,private val calendarTypeRepository: CalendarTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditNoteViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditNoteViewmodel(noteRepository,calendarTypeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
