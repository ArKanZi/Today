package com.arkanzi.today.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.arkanzi.today.model.Note
import kotlinx.serialization.Serializable

@Serializable object UsernameSetupScreenKey: NavKey
@Serializable object MainScreenKey : NavKey
@Serializable object AddNotesKey : NavKey
@Serializable object StatsScreenKey : NavKey
@Serializable object SettingsScreenKey : NavKey
@Serializable object CalendarScreenKey : NavKey
@Serializable data class NoteDetailScreenKey(val note: Note) : NavKey
@Serializable data class EditNoteScreenKey(val note: Note) : NavKey
@Serializable data class ViewAllNotesScreenKey(val name: String) : NavKey
@Serializable object SearchNotesScreenKey : NavKey