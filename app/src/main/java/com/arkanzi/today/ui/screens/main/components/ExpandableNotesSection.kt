package com.arkanzi.today.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkanzi.today.model.Note
import com.arkanzi.today.ui.components.SingleNote
import com.arkanzi.today.ui.theme.ComfortaaFontFamily
import com.arkanzi.today.util.displayTime

@Composable
fun ExpandableNotesSection(
    modifier: Modifier = Modifier,
    needBadge: Boolean = false,
    needHorizontalDivider: Boolean = true,
    noteCount: Int? = null,
    title: String,
    notes: List<Note>,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    expandedNoteId: Long?,
    deletingNoteIds: Set<Long> = emptySet(),
    onToggleCompleted: (Note) -> Unit,
    onDeleteRequest: (Note) -> Unit,
    onEditRequest: (Note) -> Unit = {},
    onFullListClick: () -> Unit = {},
    onNoteClick: (Note) -> Unit
) {


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
                .clickable { onToggleExpanded() }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Section Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f) // Take up remaining space
            ) {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = ComfortaaFontFamily,
                    color = Color.Gray
                )
                if (needBadge) {
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
                                text = noteCount?.coerceAtMost(99)
                                    .toString(), // 99+ pattern below if needed
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

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
        if (needHorizontalDivider) {
            HorizontalDivider(
                Modifier.padding(bottom = 10.dp),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )
        }

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
            Column(
                modifier = Modifier.heightIn(max = 400.dp) // Limit height
            ) {
                notes.take(5).forEach { note ->
                    val isDeleting = deletingNoteIds.contains(note.id)
                    AnimatedVisibility(
                        visible = !isDeleting, // Always visible initially
                        exit = fadeOut(
                            animationSpec = tween(500)
                        ) + shrinkVertically(
                            animationSpec = tween(500),
                            shrinkTowards = Alignment.Top
                        ),
                        modifier = Modifier.animateContentSize() // Smooth height changes
                    )
                    {
                        SingleNote(
                            note = note,
                            isCompleted = note.isCompleted,
                            title = note.title,
                            startingTime = displayTime(note.startDateTime),
                            isRevealed = expandedNoteId == note.id,
                            onCheckedChange = { onToggleCompleted(note) },
                            onDeleteRequest = { onDeleteRequest(note) },
                            onEditRequest = { onEditRequest(note) },
                            onClick = { onNoteClick(note) }
                        )
                    }
                }

                // "View All" button at the end
                if (notes.size > 5) {
                    TextButton(
                        onClick = onFullListClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "View All $title Notes",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                }
            }
        }
    }
}
