package com.arkanzi.today.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
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
import com.arkanzi.today.ui.components.NavigationBarCustom
import com.arkanzi.today.ui.components.SingleNote
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.ui.navigation.NoteDetailScreenKey
import com.arkanzi.today.ui.screens.main.components.ExpandableNotesSection
import com.arkanzi.today.ui.theme.ComfortaaFontFamily
import com.arkanzi.today.util.UserPreferences
import com.arkanzi.today.util.displayTime

@Composable
fun MainScreen(backStack: NavBackStack, noteRepository: NoteRepository,userPreferences: UserPreferences) {
    val viewModel: MainScreenViewModel =
        viewModel(factory = MainScreenViewModelFactory(noteRepository))
    val upcomingNotes by viewModel.upcomingNotes.collectAsState()
    val upcomingNotesCount by viewModel.upcomingNotesCount.collectAsState()
    val dueNotes by viewModel.dueNotes.collectAsState()
    val historyNotes by viewModel.historyNotes.collectAsState()
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
                            .size(32.dp).padding(1.dp)
                            .shadow(1.dp, shape = CircleShape, ambientColor = DefaultShadowColor, spotColor = DefaultShadowColor)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceBright).clickable{},
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

//            if (upcomingNotes.isEmpty() and dueNotes.isEmpty() and historyNotes.isEmpty()) {
//
//                Text("No notes yet.")
//
//            } else {
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
                modifier = Modifier.fillMaxWidth(),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontFamily = ComfortaaFontFamily,
                fontWeight = FontWeight.ExtraBold
            )
//            Row {
//                Box { Text("personal") }
//            }
            Row(
                modifier = Modifier.padding(bottom = 8.dp, top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                upcoming to-dos text
                Column {
                    Text(
                        "Upcoming To-Do's ",
                        modifier = Modifier,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = ComfortaaFontFamily,
                        color = Color.Gray
                    )
                }
//                Counter Badge
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Badge(
                        containerColor = Color.LightGray,
                        contentColor = Color.Gray
                    ) {
                        Text(
                            text = upcomingNotesCount.coerceAtMost(99)
                                .toString(), // 99+ pattern below if needed
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                }
            }

            LazyColumn {
                items(upcomingNotes, key = { it.id }) { note ->
                    AnimatedVisibility(
                        visible = !note.isCompleted,
                        exit = fadeOut(
                            animationSpec = tween(500)
                        ) + shrinkVertically(
                            animationSpec = tween(500)
                        ),
                        modifier = Modifier.animateItem() // For LazyColumn item animations
                    ){
                    SingleNote(
                        isCompleted = note.isCompleted,
                        title = note.title,
                        startingTime = displayTime(note.startDateTime),
                        isRevealed = viewModel.noteExtraOptionsId,
                        onCheckedChange = { viewModel.toggleCompleted(note) },
                        noteDelete = note,
                        noteRepository=noteRepository,
                        onClick={backStack.add(NoteDetailScreenKey(note)) }
                    )
                    }
                }
            }

            ExpandableNotesSection("Due's", dueNotes , visibility = true,
                iscompleted = { note -> viewModel.toggleCompleted(note) },
                noteRepository=noteRepository,
                onclick = { note -> backStack.add(NoteDetailScreenKey(note)) }
            )
            ExpandableNotesSection("History", historyNotes , visibility = true,
                iscompleted = { note -> viewModel.toggleCompleted(note) },
                noteRepository=noteRepository,
                onclick = { note -> backStack.add(NoteDetailScreenKey(note)) }
            )
//            Text("History")
//            }


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
        MainScreen(backStack, noteRepository,userPrefs)
}
