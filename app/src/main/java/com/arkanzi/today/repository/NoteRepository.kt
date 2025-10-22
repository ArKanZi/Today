package com.arkanzi.today.repository

import com.arkanzi.today.data.dao.NoteDao
import com.arkanzi.today.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    fun getTotalUpcomingCount(): Flow<Int> =
        noteDao.getTotalCountOfUpcomingNotes(System.currentTimeMillis())

    fun getUpcoming5(): Flow<List<Note>> =
        noteDao.get6UpcomingNotes(System.currentTimeMillis())

    fun getDue5(): Flow<List<Note>> =
        noteDao.get6DueNotes(System.currentTimeMillis())

    fun getHistory5(): Flow<List<Note>> =
        noteDao.get6HistoryNotes()

    suspend fun insert(note: Note) = withContext(Dispatchers.IO) { noteDao.insert(note) }

    suspend fun update(note: Note) = withContext(Dispatchers.IO) { noteDao.update(note)}
    suspend fun delete(note: Note) = withContext(Dispatchers.IO) { noteDao.delete(note)}
    suspend fun getNoteById(id: Long) = withContext(Dispatchers.IO) { noteDao.getNoteById(id)}
}