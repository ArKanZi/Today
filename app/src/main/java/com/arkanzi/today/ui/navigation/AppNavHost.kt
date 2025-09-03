package com.arkanzi.today.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.arkanzi.today.App
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.screens.addNote.AddNotesScreen
import com.arkanzi.today.ui.screens.main.MainScreen

@Composable
fun AppNavHost() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = App.DatabaseProvider.getDatabase(context)
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    val calendarTypeRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MainScreenKey>{
                MainScreen(backStack, noteRepository = noteRepository)
            }
            entry<AddNotesKey>{ entry ->
                AddNotesScreen(backStack, noteRepository = noteRepository, calendarTypeRepository = calendarTypeRepository)
            }

        }
    )
}
