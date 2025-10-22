package com.arkanzi.today.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import com.arkanzi.today.ui.screens.calendar.CalendarScreen
import com.arkanzi.today.ui.screens.editNote.EditNoteScreen
import com.arkanzi.today.ui.screens.main.MainScreen
import com.arkanzi.today.ui.screens.noteDetail.NoteDetailScreen
import com.arkanzi.today.ui.screens.settings.SettingsScreen
import com.arkanzi.today.ui.screens.stats.StatsScreen
import com.arkanzi.today.ui.screens.viewAllNotes.ViewAllNotesScreen
import com.arkanzi.today.util.UserPreferences

@Composable
fun AppNavHost() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences.getInstance(context) }
    val db = App.DatabaseProvider.getDatabase(context)
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    val calendarTypeRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<MainScreenKey> {
                MainScreen(backStack, noteRepository, userPrefs)
            }
            entry<AddNotesKey>(
                metadata = NavDisplay.transitionSpec {
                    // Slide up from bottom when entering
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(400, easing = EaseOutQuart)
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    // Slide down to bottom when leaving
                    EnterTransition.None togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(400, easing = EaseInQuart)
                    )
                }
            ) {
                AddNotesScreen(backStack, noteRepository, calendarTypeRepository)
            }

            entry<EditNoteScreenKey>(
                metadata = NavDisplay.transitionSpec {
                    // Slide up from bottom when entering
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(400, easing = EaseOutQuart)
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    // Slide down to bottom when leaving
                    EnterTransition.None togetherWith slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(400, easing = EaseInQuart)
                    )
                }
            ) {
                EditNoteScreen(backStack, noteRepository, calendarTypeRepository, note = it.note)
            }

            entry<ViewAllNotesScreenKey> {
                ViewAllNotesScreen(backStack = backStack, noteRepository = noteRepository, notesType = it.name)
            }
            entry<StatsScreenKey> {
                StatsScreen(backStack, noteRepository)
            }
            entry<SettingsScreenKey> {
                SettingsScreen(backStack, userPrefs)
            }
            entry<CalendarScreenKey> {
                CalendarScreen(backStack, noteRepository)
            }
            entry<NoteDetailScreenKey> {
                NoteDetailScreen(backStack, calendarTypeRepository, note = it.note)
            }

        }
    )
}
