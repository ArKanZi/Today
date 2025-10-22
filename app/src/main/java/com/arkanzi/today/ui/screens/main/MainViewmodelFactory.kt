package com.arkanzi.today.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkanzi.today.repository.NoteRepository

class MainViewmodelFactory(
    private val noteRepository: NoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewmodel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
