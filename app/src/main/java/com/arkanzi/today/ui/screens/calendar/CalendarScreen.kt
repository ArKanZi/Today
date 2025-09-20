package com.arkanzi.today.ui.screens.calendar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.NavigationBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout

@Composable
fun CalendarScreen(
    backStack: NavBackStack,
    noteRepository: NoteRepository
){
    DefaultLayout(
        bottomBar = { NavigationBarCustom(backStack) },
    ){
        Text("Coming Soon")
    }
}