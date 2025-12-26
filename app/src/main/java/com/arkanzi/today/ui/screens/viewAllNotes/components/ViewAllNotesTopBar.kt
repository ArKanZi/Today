package com.arkanzi.today.ui.screens.viewAllNotes.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arkanzi.today.R
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.screens.viewAllNotes.ViewAllNotesViewmodel
import com.arkanzi.today.ui.screens.viewAllNotes.ViewAllNotesViewmodelFactory

@Composable
fun ViewAllNotesTopBar(
    backStack: NavBackStack<NavKey>,
    viewmodel: ViewAllNotesViewmodel
) {


    val screenTitle by viewmodel.screenTitle.collectAsState()
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