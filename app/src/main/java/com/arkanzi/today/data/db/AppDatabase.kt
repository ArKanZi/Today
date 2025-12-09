package com.arkanzi.today.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arkanzi.today.data.dao.CalendarTypeDao
import com.arkanzi.today.data.dao.NoteDao
import com.arkanzi.today.model.CalendarType
import com.arkanzi.today.model.Note

@Database(entities = [Note::class, CalendarType::class], version = 10, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao
    abstract fun calendarTypeDao(): CalendarTypeDao
}