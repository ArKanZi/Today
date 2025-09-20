package com.arkanzi.today.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.arkanzi.today.model.Note
import kotlinx.serialization.Serializable

@Serializable object MainScreenKey : NavKey
@Serializable object AddNotesKey : NavKey
@Serializable object StatsScreenKey : NavKey
@Serializable object SettingsScreenKey : NavKey
@Serializable object CalendarScreenKey : NavKey
@Serializable data class NoteDetailScreenKey(val note: Note) : NavKey