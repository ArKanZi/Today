package com.arkanzi.today.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = CalendarType::class,
            parentColumns = ["id"],
            childColumns = ["calendarTypeId"],
            onDelete = ForeignKey.SET_NULL,   // or CASCADE/RESTRICT
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index("calendarTypeId")])
data class Note (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String,
    var place: String = "",
    var dueDate: Long = System.currentTimeMillis(),
    var startTime: Long = System.currentTimeMillis(),
    var endTime: Long = System.currentTimeMillis(),
    var note: String = "",
    var priority: String = "Normal",
    var calendarTypeId: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false
    )

