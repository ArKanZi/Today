package com.arkanzi.today.ui.screens.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.arkanzi.today.App
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.ConfirmationDialog
import com.arkanzi.today.ui.components.NavigationBarCustom
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.EditNoteScreenKey
import com.arkanzi.today.ui.navigation.NoteDetailScreenKey
import com.arkanzi.today.ui.navigation.SearchNotesScreenKey
import com.arkanzi.today.ui.navigation.ViewAllNotesScreenKey
import com.arkanzi.today.ui.screens.main.components.ExpandableNotesSection
import com.arkanzi.today.ui.screens.search.components.CustomSearchBar
import kotlinx.coroutines.launch


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    backStack: NavBackStack<NavKey>,
    viewModel: SearchViewModel
) {
    val deletingNoteIds by viewModel.deletingNoteIds.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val expandedSections by viewModel.expandedSections.collectAsState()
    val expandedNoteId by viewModel.expandedNoteId.collectAsState()

    val upcomingNotes by viewModel.upcomingNotes.collectAsState()
    val dueNotes by viewModel.dueNotes.collectAsState()
    val historyNotes by viewModel.historyNotes.collectAsState()

    val results by viewModel.results.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            if (upcomingNotes.isNotEmpty()) {
                item(key = "upcoming_section") {
                    ExpandableNotesSection(
                        title = "Upcoming To-Do's",
                        needBadge = true,
                        noteCount = upcomingNotes.size,
                        notes = upcomingNotes,
                        needHorizontalDivider = false,
                        isExpanded = expandedSections.contains("upcoming"),
                        onToggleExpanded = {
                            viewModel.toggleSectionExpanded("upcoming")
                        },
                        deletingNoteIds = deletingNoteIds,
                        expandedNoteId = expandedNoteId,
                        onNoteClick = { note -> backStack.add(NoteDetailScreenKey(note)) },
                        onToggleCompleted = { note -> viewModel.toggleCompleted(note) },
                        onDeleteRequest = { viewModel.showDeleteConfirmation(it) },
                        onEditRequest = { backStack.add(EditNoteScreenKey(it)) },
                        fullListNeeded = true,
                        onFullListClick = {
                            viewModel.viewModelScope.launch {
                                backStack.add(ViewAllNotesScreenKey("upcoming"))
                            }
                        }
                    )
                }
            }

            if (dueNotes.isNotEmpty()) {
                item(key = "due_section") {
                    ExpandableNotesSection(
                        title = "Due's",
                        needBadge = true,
                        noteCount = dueNotes.size,
                        notes = dueNotes,
                        isExpanded = expandedSections.contains("due"),
                        onToggleExpanded = {
                            viewModel.toggleSectionExpanded("due")
                        },
                        deletingNoteIds = deletingNoteIds,
                        expandedNoteId = expandedNoteId,
                        onNoteClick = { note -> backStack.add(NoteDetailScreenKey(note)) },
                        onToggleCompleted = { note -> viewModel.toggleCompleted(note) },
                        onDeleteRequest = { viewModel.showDeleteConfirmation(it) },
                        onEditRequest = { backStack.add(EditNoteScreenKey(it)) },
                        fullListNeeded = true,
                        onFullListClick = {
                            viewModel.viewModelScope.launch {
                                backStack.add(ViewAllNotesScreenKey("due"))
                            }
                        }
                    )
                }
            }

            if (historyNotes.isNotEmpty()) {
                item(key = "history_section") {
                    ExpandableNotesSection(
                        title = "History",
                        needBadge = true,
                        noteCount = historyNotes.size,
                        notes = historyNotes,
                        isExpanded = expandedSections.contains("history"),
                        onToggleExpanded = {
                            viewModel.toggleSectionExpanded("history")
                        },
                        expandedNoteId = expandedNoteId,
                        onNoteClick = { note -> backStack.add(NoteDetailScreenKey(note)) },
                        onToggleCompleted = { note -> viewModel.toggleCompleted(note) },
                        onDeleteRequest = { viewModel.showDeleteConfirmation(it) },
                        onEditRequest = { backStack.add(EditNoteScreenKey(it)) },
                        fullListNeeded = true,
                        onFullListClick = {
                            viewModel.viewModelScope.launch {
                                backStack.add(ViewAllNotesScreenKey("history"))
                            }
                        }
                    )
                }
            }

        }
        ConfirmationDialog(
            showDialog = showDeleteDialog,
            onConfirm = viewModel::onDeleteConfirm,
            onDismiss = viewModel::onDeleteDismiss
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun Material3AnimatedSearchBarPreview() {

    val backStack = rememberNavBackStack(SearchNotesScreenKey)
    val context = LocalContext.current
    val db = App.DatabaseProvider.getDatabase(context)
    val noteRepository = remember { NoteRepository(db.noteDao()) }

    SharedTransitionLayout {
        SearchScreen(backStack, viewModel = SearchViewModel(noteRepository))
    }
}