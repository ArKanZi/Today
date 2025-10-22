package com.arkanzi.today.ui.screens.main

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
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
import com.arkanzi.today.ui.screens.editNote.EditNoteScreen
import com.arkanzi.today.ui.screens.main.components.ExpandableNotesSection
import com.arkanzi.today.ui.theme.ComfortaaFontFamily
import com.arkanzi.today.util.UserPreferences

@Composable
fun MainScreen(
    backStack: NavBackStack,
    noteRepository: NoteRepository,
    userPreferences: UserPreferences
) {
    val viewModel: MainViewmodel =
        viewModel(factory = MainViewmodelFactory(noteRepository))
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val expandedSections by viewModel.expandedSections.collectAsState()
    val expandedNoteId by viewModel.expandedNoteId.collectAsState()
    val upcomingNotes by viewModel.upcomingNotes.collectAsState()
    val upcomingNotesCount by viewModel.upcomingNotesCount.collectAsState()
    val dueNotes by viewModel.dueNotes.collectAsState()
    val historyNotes by viewModel.historyNotes.collectAsState()
    val deletingNoteIds by viewModel.deletingNoteIds.collectAsState()
    DefaultLayout(
        topBar = {
            TopAppBarCustom(
                leftContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_horizontal_menu),
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                    )
                },
                rightContent = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .padding(1.dp)
                            .shadow(
                                1.dp,
                                shape = CircleShape,
                                ambientColor = DefaultShadowColor,
                                spotColor = DefaultShadowColor
                            )
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceBright)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_magnifying_glass),
                            contentDescription = "Search",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                })
        },
        bottomBar = { NavigationBarCustom(backStack) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp)
        ) {

            if (upcomingNotes.isEmpty() and dueNotes.isEmpty() and historyNotes.isEmpty()) {

                Text("No notes yet.")

            } else {
                Text(
                    "Hey ${userPreferences.getUserName()}",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontFamily = ComfortaaFontFamily,
                    color = Color.Gray
                )
                Text(
                    "what's your plan?",
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontFamily = ComfortaaFontFamily,
                    fontWeight = FontWeight.ExtraBold
                )
//            Row {
//                Box { Text("personal") }
//            }

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
                                onEditRequest = { backStack.add(EditNoteScreenKey(it)) }
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
                                onEditRequest = { backStack.add(EditNoteScreenKey(it)) }
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
                                onEditRequest = { backStack.add(EditNoteScreenKey(it)) }
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

@Preview
@Composable
fun MainScreenPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = App.DatabaseProvider.getDatabase(context)
    val userPrefs = remember { UserPreferences.getInstance(context) }
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    MainScreen(backStack, noteRepository, userPrefs)
}
