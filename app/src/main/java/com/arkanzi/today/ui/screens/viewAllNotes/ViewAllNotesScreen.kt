package com.arkanzi.today.ui.screens.viewAllNotes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.arkanzi.today.ui.components.SingleNote
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.EditNoteScreenKey
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.ui.navigation.NoteDetailScreenKey
import com.arkanzi.today.util.displayTime

@Composable
fun ViewAllNotesScreen(
    backStack: NavBackStack,
    noteRepository: NoteRepository,
    notesType: String
) {
    val viewModel: ViewAllNotesViewmodel = viewModel(
        key = "notesType_$notesType",
        factory = ViewAllNotesViewmodelFactory(noteRepository, notesType)
    )


    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    val notes by viewModel.notes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val screenTitle by viewModel.screenTitle.collectAsState()
    val deletingNoteIds by viewModel.deletingNoteIds.collectAsState()
    val expandedNoteId by viewModel.expandedNoteId.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()

    DefaultLayout(
        topBar = {
            TopAppBarCustom(
                leftContent = {
                    IconButton(
                        onClick = { backStack.removeLastOrNull() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross),
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                centerContent = {
                    Text(
                        "All $screenTitle",
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No notes found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notes, key = { it.id }) { note ->
                        val isDeleting = deletingNoteIds.contains(note.id)

                        AnimatedVisibility(
                            visible = !isDeleting,
                            exit = fadeOut(
                                animationSpec = tween(500)
                            ) + shrinkVertically(
                                animationSpec = tween(500),
                                shrinkTowards = Alignment.Top
                            ),
                            modifier = Modifier.animateContentSize()
                        ) {
                            SingleNote(
                                note = note,
                                isCompleted = note.isCompleted,
                                title = note.title,
                                startingTime = displayTime(note.startDateTime),
                                isRevealed = expandedNoteId == note.id,
                                onCheckedChange = { viewModel.toggleCompleted(note) },
                                onDeleteRequest = { viewModel.showDeleteConfirmation(note) },
                                onEditRequest = { backStack.add(EditNoteScreenKey(note)) },
                                onClick = { backStack.add(NoteDetailScreenKey(note)) }
                            )
                        }
                    }
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

@Preview
@Composable
fun ViewAllNotesScreenPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = App.DatabaseProvider.getDatabase(context)
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    ViewAllNotesScreen(backStack, noteRepository, "upcoming")
}
