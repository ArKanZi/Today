package com.arkanzi.today.ui.screens.search.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    onSearchQueryChange: (String) -> Unit = {},
    onSearchTriggered: () -> Unit = {},
    backAction: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    val animationDuration = 0
    val fadeAnimation = tween<Float>(animationDuration, easing = LinearOutSlowInEasing)

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back / Search icon
                IconButton(
                    onClick = {
                        isExpanded = !isExpanded
                    }
                ) {
                    AnimatedContent(
                        targetState = isExpanded,
                        transitionSpec = {
                            fadeIn(fadeAnimation) togetherWith fadeOut(fadeAnimation)
                        },
                        label = "SearchIcon"
                    ) { expanded ->
                        Icon(
                            imageVector = if (expanded)
                                Icons.AutoMirrored.Filled.ArrowBack
                            else
                                Icons.Default.Search,
                            contentDescription = if (expanded) "Back" else "Search",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Input and clear button
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(tween(250)) + slideInHorizontally(
                        initialOffsetX = { it / 6 },
                        animationSpec = tween(250)
                    ),
                    exit = fadeOut(tween(200)) + slideOutHorizontally(
                        targetOffsetX = { it / 6 },
                        animationSpec = tween(200)
                    )
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                onSearchQueryChange(it)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = { onSearchTriggered() }
                            ),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Search...",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                innerTextField()
                            }
                        )

                        // Clear button
                        Box(
                            modifier = Modifier.width(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = searchQuery.isNotEmpty(),
                                enter = fadeIn(tween(200)) + scaleIn(initialScale = 0.8f),
                                exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.8f)
                            ) {
                                IconButton(
                                    onClick = {
                                        searchQuery = ""
                                        onSearchQueryChange("")
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Delay navigation until collapse animation completes
    LaunchedEffect(isExpanded) {
        if (!isExpanded) {
            delay(animationDuration.toLong())
            backAction()
        }
    }
}
