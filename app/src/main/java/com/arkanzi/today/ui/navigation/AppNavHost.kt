package com.arkanzi.today.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
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
import com.arkanzi.today.ui.screens.search.SearchScreen
import com.arkanzi.today.ui.screens.settings.SettingsScreen
import com.arkanzi.today.ui.screens.stats.StatsScreen
import com.arkanzi.today.ui.screens.viewAllNotes.ViewAllNotesScreen
import com.arkanzi.today.util.UserPreferences

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences.getInstance(context) }
    val db = App.DatabaseProvider.getDatabase(context)
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    val calendarTypeRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }

    // ✅ SharedTransitionLayout wraps the entire NavDisplay
    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {


                entry<MainScreenKey>(
                    metadata = NavDisplay.transitionSpec {
                        fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
                    }
                ) {
                    MainScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        backStack = backStack,
                        noteRepository = noteRepository,
                        userPreferences = userPrefs,
                    )
                }

                // 🟣 Search Screen (contains shared search bar)
                // ✅ fadeIn/fadeOut transition gives overlap for shared-element
                entry<SearchNotesScreenKey>(
                    metadata = NavDisplay.transitionSpec {
                        fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
                    } + NavDisplay.popTransitionSpec {
                        fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
                    }
                ) {
                    SearchScreen(this@SharedTransitionLayout,backStack)
                }

                // 🟧 Add Notes (slide-up animation)
                entry<AddNotesKey>(
                    metadata = NavDisplay.transitionSpec {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400, easing = EaseOutQuart)
                        ) togetherWith fadeOut(animationSpec = tween(300))
                    } + NavDisplay.popTransitionSpec {
                        fadeIn(animationSpec = tween(200)) togetherWith slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(400, easing = EaseInQuart)
                        )
                    }
                ) {
                    AddNotesScreen(backStack, noteRepository, calendarTypeRepository)
                }

                // 🟨 Edit Note Screen (same slide pattern)
                entry<EditNoteScreenKey>(
                    metadata = NavDisplay.transitionSpec {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(400, easing = EaseOutQuart)
                        ) togetherWith fadeOut(animationSpec = tween(300))
                    } + NavDisplay.popTransitionSpec {
                        fadeIn(animationSpec = tween(200)) togetherWith slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(400, easing = EaseInQuart)
                        )
                    }
                ) {
                    EditNoteScreen(backStack, noteRepository, calendarTypeRepository, note = it.note)
                }

                // 🟦 Other non-shared screens
                entry<ViewAllNotesScreenKey> {
                    ViewAllNotesScreen(backStack, noteRepository, notesType = it.name)
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
}

