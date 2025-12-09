package com.arkanzi.today.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = CalendarType::class,
            parentColumns = ["id"],
            childColumns = ["calendarTypeId"],
            onDelete = ForeignKey.NO_ACTION,   // or CASCADE/RESTRICT
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index("calendarTypeId"),
        Index(value = ["endDateTime"]),
        Index(value = ["isCompleted"]),
        Index(value = ["startDateTime"]),
        Index(value = ["endDateTime", "isCompleted"])]
)
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String,
    var place: String = "",
    var startDateTime: Long = System.currentTimeMillis(),
    var endDateTime: Long = System.currentTimeMillis(),
    var note: String = "",
    var priority: String = "Normal",
    var calendarTypeId: Long,
    val createdAt: Long = System.currentTimeMillis(),
    var isCompleted: Boolean = false,
    val isNotificationOn: Boolean = false
)

