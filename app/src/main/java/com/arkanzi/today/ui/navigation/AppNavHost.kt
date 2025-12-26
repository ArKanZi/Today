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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.arkanzi.today.App
import com.arkanzi.today.repository.CalendarTypeRepository
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.NavigationBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.layout.ScreenChrome
import com.arkanzi.today.ui.screens.addNote.AddNotesScreen
import com.arkanzi.today.ui.screens.addNote.components.AddNoteTopBar
import com.arkanzi.today.ui.screens.editNote.EditNoteScreen
import com.arkanzi.today.ui.screens.editNote.components.EditNoteTopBar
import com.arkanzi.today.ui.screens.main.MainScreen
import com.arkanzi.today.ui.screens.main.components.MainTopBar
import com.arkanzi.today.ui.screens.noteDetail.NoteDetailScreen
import com.arkanzi.today.ui.screens.noteDetail.components.NoteDetailTopBar
import com.arkanzi.today.ui.screens.search.SearchScreen
import com.arkanzi.today.ui.screens.search.SearchViewModel
import com.arkanzi.today.ui.screens.search.SearchViewmodelFactory
import com.arkanzi.today.ui.screens.search.components.SearchTopBar
import com.arkanzi.today.ui.screens.settings.SettingsScreen
import com.arkanzi.today.ui.screens.settings.components.SettingsTopBar
import com.arkanzi.today.ui.screens.user.UsernameSetupScreen
import com.arkanzi.today.ui.screens.viewAllNotes.ViewAllNotesViewmodel
import com.arkanzi.today.ui.screens.viewAllNotes.ViewAllNotesViewmodelFactory
import com.arkanzi.today.ui.screens.viewAllNotes.ViewAllNotesScreen
import com.arkanzi.today.ui.screens.viewAllNotes.components.ViewAllNotesTopBar
import com.arkanzi.today.util.UserPreferences

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences.getInstance(context) }
    val username = userPrefs.getUserName()
    val db = App.DatabaseProvider.getDatabase(context)
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    val calendarTypeRepository = remember { CalendarTypeRepository(db.calendarTypeDao()) }
    val startKey =
        if (username.isEmpty()) UsernameSetupScreenKey
        else MainScreenKey

    val backStack = rememberNavBackStack(startKey)

    val chromeState = remember { mutableStateOf(ScreenChrome()) }

    // ✅ SharedTransitionLayout wraps the entire NavDisplay
    SharedTransitionLayout {
        DefaultLayout(chrome = chromeState.value) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {

                    entry<UsernameSetupScreenKey> {
                        UsernameSetupScreen(backStack, userPrefs)
                    }

                    entry<MainScreenKey>(
                        metadata = NavDisplay.transitionSpec {
                            fadeIn(animationSpec = tween(250)) togetherWith fadeOut(
                                animationSpec = tween(
                                    250
                                )
                            )
                        }
                    ) {
                        val animScope = LocalNavAnimatedContentScope.current
                        chromeState.value = ScreenChrome(
                            topBar = {
                                MainTopBar(
                                    backStack,
                                    animScope,
                                    this@SharedTransitionLayout
                                )
                            },
                            bottomBar = { NavigationBarCustom(backStack) }
                        )
                        MainScreen(
                            backStack = backStack,
                            noteRepository = noteRepository,
                            userPreferences = userPrefs,
                        )
                    }

                    // 🟣 Search Screen (contains shared search bar)
                    // ✅ fadeIn/fadeOut transition gives overlap for shared-element
                    entry<SearchNotesScreenKey>(
                        metadata = NavDisplay.transitionSpec {
                            fadeIn(animationSpec = tween(250)) togetherWith fadeOut(
                                animationSpec = tween(
                                    250
                                )
                            )
                        } + NavDisplay.popTransitionSpec {
                            fadeIn(animationSpec = tween(250)) togetherWith fadeOut(
                                animationSpec = tween(
                                    250
                                )
                            )
                        }
                    ) {
                        val viewModel : SearchViewModel = viewModel(factory = SearchViewmodelFactory(noteRepository))
                        val animScope = LocalNavAnimatedContentScope.current
                        chromeState.value = ScreenChrome(
                            topBar = {
                                SearchTopBar(
                                    backStack,
                                    animScope,
                                    this@SharedTransitionLayout,
                                    viewModel = viewModel
                                )
                            },
                            bottomBar = { NavigationBarCustom(backStack) }
                        )
                        SearchScreen(backStack,viewModel)
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
                    chromeState.value = ScreenChrome(
                        topBar = {
                            AddNoteTopBar(backStack)
                        },
                        bottomBar = null
                    )
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
                    chromeState.value = ScreenChrome(
                        topBar = {
                            EditNoteTopBar(backStack)
                        },
                        bottomBar = null
                    )
                    EditNoteScreen(
                        backStack,
                        noteRepository,
                        calendarTypeRepository,
                        note = it.note
                    )
                }

                    // 🟦 Other non-shared screens
                entry<ViewAllNotesScreenKey> {
                    val viewModel: ViewAllNotesViewmodel = viewModel(
                        key = "notesType_${it.name}",
                        factory = ViewAllNotesViewmodelFactory(noteRepository, it.name)
                    )
                    chromeState.value = ScreenChrome(
                        topBar = { ViewAllNotesTopBar(backStack,viewModel) },
                        bottomBar = null
                    )
                    ViewAllNotesScreen(backStack,viewModel)
                }
                    entry<SettingsScreenKey> {
                        chromeState.value = ScreenChrome(
                            topBar = { SettingsTopBar(backStack) },
                            bottomBar = { NavigationBarCustom(backStack) }
                        )
                        SettingsScreen(userPrefs)
                    }
                entry<NoteDetailScreenKey> {
                    chromeState.value = ScreenChrome(
                        topBar = { NoteDetailTopBar(backStack) },
                        bottomBar = null
                    )
                    NoteDetailScreen(calendarTypeRepository = calendarTypeRepository, note = it.note)
                }
                }
            )
        }
    }
}

