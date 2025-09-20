package com.arkanzi.today.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkanzi.today.model.Note
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.SingleNote
import com.arkanzi.today.ui.theme.ComfortaaFontFamily
import com.arkanzi.today.util.displayTime
@Composable
fun ExpandableNotesSection(
    title: String,
    notes: List<Note>,
    modifier: Modifier = Modifier,
    visibility: Boolean,
    iscompleted: (Note)-> Unit,
    fullList:()-> Unit = {},
    noteRepository: NoteRepository,
    onclick:(Note)-> Unit
) {
    // State for expansion
    var isExpanded by remember { mutableStateOf(false) }

    // Rotation animation for arrow
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f, // 45 degrees rotation
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "Arrow rotation"
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Header with title and rotating arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Section Title
            Text(
                text = title,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = ComfortaaFontFamily,
                color = Color.Gray
            )

            // Rotating Arrow Icon
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // Use your arrow icon
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(arrowRotation) // Apply rotation animation
            )
        }
        HorizontalDivider(
            Modifier.padding(bottom = 10.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.3f)
        )

        // Expandable Content with Animation
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(
                animationSpec = tween(300)
            ) + expandVertically(
                animationSpec = tween(300),
                expandFrom = Alignment.Top
            ),
            exit = fadeOut(
                animationSpec = tween(300)
            ) + shrinkVertically(
                animationSpec = tween(300),
                shrinkTowards = Alignment.Top
            )
        ) {
            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp) // Limit height
            ) {
                items(items=notes, key = { it.id }) { note ->
                    AnimatedVisibility(
                        visible = visibility,
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
                        isRevealed = false,
                        onCheckedChange = { iscompleted(note) },
                        noteDelete = note,
                        noteRepository=noteRepository,
                        onClick = { onclick(note) }
                    )}
                }

                // "View All" button at the end
                if(notes.size > 5){
                    item {
                        TextButton(
                            onClick = fullList,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "View All Notes",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
