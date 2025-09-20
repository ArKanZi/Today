package com.arkanzi.today.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.arkanzi.today.model.CalendarType
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarTypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(type: CalendarType): Long

    @Query("SELECT * FROM calendar_types WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): CalendarType?

    @Query("SELECT * FROM calendar_types WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): CalendarType?

    @Query("SELECT * FROM calendar_types ORDER BY name ASC")
    fun getAll(): Flow<List<CalendarType>>

    @Transaction
    suspend fun getOrCreate(name: String): CalendarType {
        val existing = findByName(name)
        if (existing != null) return existing
        val id = insertIgnore(CalendarType(name = name))
        return if (id > 0) CalendarType(name = name)
        else findByName(name)!! // race-safe if created concurrently
    }
}
