package com.arkanzi.today.repository

import com.arkanzi.today.data.dao.NoteDao
import com.arkanzi.today.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    fun getTotalUpcomingCount(): Flow<Int> = noteDao.getTotalCountOfUpcomingNotes(System.currentTimeMillis())

    fun get6Upcoming(): Flow<List<Note>> = noteDao.get6UpcomingNotes(System.currentTimeMillis())

    fun getAllUpcoming(): Flow<List<Note>> = noteDao.getAllUpcomingNotes(System.currentTimeMillis())

    fun get6Due(): Flow<List<Note>> = noteDao.get6DueNotes(System.currentTimeMillis())

    fun getAllDue(): Flow<List<Note>> = noteDao.getAllDueNotes(System.currentTimeMillis())

    fun get6History(): Flow<List<Note>> = noteDao.get6HistoryNotes()

    fun getAllHistory(): Flow<List<Note>> = noteDao.getAllHistoryNotes()

    fun getSearchNotes(query: String): Flow<List<Note>> = noteDao.searchNotes(query)
    suspend fun insert(note: Note): Long = noteDao.insert(note)
    suspend fun update(note: Note) = noteDao.update(note)
    suspend fun delete(note: Note) = noteDao.delete(note)
    suspend fun getNoteById(id: Long): Note? = noteDao.getNoteById(id)
}
