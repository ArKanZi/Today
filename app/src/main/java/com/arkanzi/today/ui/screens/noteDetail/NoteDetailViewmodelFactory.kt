package com.arkanzi.today.ui.screens.noteDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkanzi.today.repository.CalendarTypeRepository

class NoteDetailViewmodelFactory(
    private val calendarTypeRepository: CalendarTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteDetailViewmodel(calendarTypeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}