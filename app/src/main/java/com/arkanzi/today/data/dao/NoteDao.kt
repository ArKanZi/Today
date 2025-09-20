package com.arkanzi.today.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.arkanzi.today.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT COUNT(*) FROM notes Where endDateTime > :currentTime AND isCompleted = 0")
    fun getTotalCountOfUpcomingNotes(currentTime:Long): Flow<Int>

    @Query("SELECT * FROM notes Where endDateTime > :currentTime AND isCompleted = 0 ORDER BY startDateTime ASC LIMIT 5")
    fun get5UpcomingNotes(currentTime: Long): Flow<List<Note>>

    @Query("SELECT * FROM notes Where endDateTime < :currentTime AND isCompleted = 0 ORDER BY startDateTime ASC LIMIT 5")
    fun get5DueNotes(currentTime: Long): Flow<List<Note>>

    @Query("SELECT * FROM notes Where isCompleted = 1 ORDER BY startDateTime DESC LIMIT 5")
    fun get5HistoryNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): Note?
}