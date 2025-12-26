package com.arkanzi.today.ui.screens.viewAllNotes

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.arkanzi.today.App
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.ConfirmationDialog
import com.arkanzi.today.ui.components.SingleNote
import com.arkanzi.today.ui.navigation.EditNoteScreenKey
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.ui.navigation.NoteDetailScreenKey
import com.arkanzi.today.util.displayTime

@Composable
fun ViewAllNotesScreen(
    backStack: NavBackStack<NavKey>,
    viewmodel: ViewAllNotesViewmodel
) {


    LaunchedEffect(Unit) {
        viewmodel.loadNotes()
    }

    val notes by viewmodel.notes.collectAsState()
    val isLoading by viewmodel.isLoading.collectAsState()
    val deletingNoteIds by viewmodel.deletingNoteIds.collectAsState()
    val expandedNoteId by viewmodel.expandedNoteId.collectAsState()
    val showDeleteDialog by viewmodel.showDeleteDialog.collectAsState()

    Column(modifier = Modifier.fillMaxSize()){
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
                            onCheckedChange = { viewmodel.toggleCompleted(note) },
                            onDeleteRequest = { viewmodel.showDeleteConfirmation(note) },
                            onEditRequest = { backStack.add(EditNoteScreenKey(note)) },
                            onClick = { backStack.add(NoteDetailScreenKey(note)) }
                        )
                    }
                }
            }
        }
        ConfirmationDialog(
            showDialog = showDeleteDialog,
            onConfirm = viewmodel::onDeleteConfirm,
            onDismiss = viewmodel::onDeleteDismiss
        )
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun ViewAllNotesScreenPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val db = App.DatabaseProvider.getDatabase(context)
    val noteRepository = remember { NoteRepository(db.noteDao()) }
    ViewAllNotesScreen(backStack, ViewAllNotesViewmodel(noteRepository,""))
}
