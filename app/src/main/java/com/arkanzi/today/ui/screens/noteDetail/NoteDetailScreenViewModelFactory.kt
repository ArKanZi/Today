package com.arkanzi.today.ui.screens.noteDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkanzi.today.repository.CalendarTypeRepository

class NoteDetailScreenViewModelFactory(
    private val calendarTypeRepository: CalendarTypeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteDetailScreenViewModel(calendarTypeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}