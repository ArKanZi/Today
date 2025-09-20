package com.arkanzi.today.repository

import com.arkanzi.today.data.dao.CalendarTypeDao
import com.arkanzi.today.model.CalendarType
import kotlinx.coroutines.flow.Flow

class CalendarTypeRepository(private val calendarType: CalendarTypeDao) {
    val allCalendarType : Flow<List<CalendarType>> = calendarType.getAll()
    val findById : suspend (Long) -> CalendarType? = { id -> calendarType.findById(id) }
}