package com.arkanzi.today.ui.screens.noteDetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkanzi.today.repository.CalendarTypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewmodel(
    private val calendarTypeRepository: CalendarTypeRepository
) : ViewModel() {

    private val _calendarName = MutableStateFlow("")
    val calendarName: StateFlow<String> = _calendarName.asStateFlow()

    fun loadCalendarName(id: Long) {
        viewModelScope.launch {
            _calendarName.value = calendarTypeRepository.findById(id)?.name ?: ""
        }
    }

    var isNotification : Boolean by mutableStateOf(false)
}

