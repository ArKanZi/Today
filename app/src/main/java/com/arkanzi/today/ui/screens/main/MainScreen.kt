package com.arkanzi.today.ui.screens.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.arkanzi.today.App
import com.arkanzi.today.R
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.ConfirmationDialog
import com.arkanzi.today.ui.components.NavigationBarCustom
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.EditNoteScreenKey
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.ui.navigation.NoteDetailScreenKey
import com.arkanzi.today.ui.navigation.SearchNotesScreenKey
import com.arkanzi.today.ui.navigation.ViewAllNotesScreenKey
import com.arkanzi.today.ui.screens.main.components.ExpandableNotesSection
import com.arkanzi.today.ui.theme.ComfortaaFontFamily
import com.arkanzi.today.util.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
    noteRepository: NoteRepository,
    userPreferences: UserPreferences,
) {
    val viewModel: MainViewmodel =
        viewModel(factory = MainViewmodelFactory(noteRepository))

    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val expandedSections by viewModel.expandedSections.collectAsState()
    val expandedNoteId by viewModel.expandedNoteId.collectAsState()
    val upcomingNotes by viewModel.upcoming6Notes.collectAsState()
    val upcomingNotesCount by viewModel.upcomingNotesCount.collectAsState()
    val dueNotes by viewModel.due6Notes.collectAsState()
    val historyNotes by viewModel.history6Notes.collectAsState()
    val deletingNoteIds by viewModel.deletingNoteIds.collectAsState()

    with(sharedTransitionScope) {
        DefaultLayout(
            topBar = {
                TopAppBarCustom(
                    fullWidth = false,
                    leftContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_horizontal_menu),
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    rightContent = {
                        // 🔹 Shared transition search icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceBright)
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "search_bar"),
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                )
                                .clickable {
                                    backStack.add(SearchNotesScreenKey)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_magnifying_glass),
                                contentDescription = "Search",
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            },
            bottomBar = { NavigationBarCustom(backStack) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp)
            ) {
                if (upcomingNotes.isEmpty() && dueNotes.isEmpty() && historyNotes.isEmpty()) {
                    Text("No notes yet.")
                } else {
                    Text(
                        "Hey ${userPreferences.getUserName()}",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontFamily = ComfortaaFontFamily,
                        color = Color.Gray
                    )
                    Text(
                        "what's your plan?",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontFamily = ComfortaaFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )

                    LazyColumn {
                        if (upcomingNotes.isNotEmpty()) {
                            item(key = "upcoming_section") {
                                ExpandableNotesSection(
                                    title = "Upcoming To-Do's",
                                    needBadge = true,
                                    notes = upcomingNotes,
                                    needHorizontalDivider = false,
                                    isExpanded = expandedSections.contains("upcoming"),
                                    noteCount = upcomingNotesCount,
                                    onToggleExpanded = {
                                        viewModel.toggleSectionExpanded("upcoming")
                                    },
                                    deletingNoteIds = deletingNoteIds,
                                    expandedNoteId = expandedNoteId,
                                    onNoteClick = { note -> backStack.add(NoteDetailScreenKey(note)) },
                                    onToggleCompleted = { note -> viewModel.toggleCompleted(note) },
                                    onDeleteRequest = { viewModel.showDeleteConfirmation(it) },
                                    onEditRequest = { backStack.add(EditNoteScreenKey(it)) },
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
                                    onFullListClick = {
                                        viewModel.viewModelScope.launch {
                                            backStack.add(ViewAllNotesScreenKey("history"))
                                        }
                                    }
                                )
                            }
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
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun MainScreenPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = App.DatabaseProvider.getDatabase(context)
    val userPrefs = remember { UserPreferences.getInstance(context) }
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    SharedTransitionLayout {
        MainScreen(this@SharedTransitionLayout, backStack, noteRepository, userPrefs)
    }
}
