package com.arkanzi.today.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calendar_types",
    indices = [Index(value = ["name"], unique = true)]
)
data class CalendarType(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)

