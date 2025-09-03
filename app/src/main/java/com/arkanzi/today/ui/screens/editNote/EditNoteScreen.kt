package com.arkanzi.today.ui.screens.editNote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout

@Composable
fun EditNotesScreen(todoId:String) {
    DefaultLayout(
        topBar = {
            TopAppBarCustom(
                { Text("X") },
                { Text("Add note") }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Title")
            TextField(value = "Title of the Note", onValueChange = {})
            Text("Place")
            TextField(value = "Place", onValueChange = {})
            Text("Time")
            TextField(value = "Time", onValueChange = {})
            Text("Notes")
            TextField(value = "Note", onValueChange = {})
            DropdownMenu(expanded = false, onDismissRequest = {}) { }
            Text("Calender")
            DropdownMenu(expanded = false, onDismissRequest = {}) { }
            Text("Alarm")
            Button(onClick = {}) {Text("Add") }

        }

    }
}

@Preview
@Composable
fun EditNotesPreview() {
    EditNotesScreen("abc")
}
