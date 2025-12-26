package com.arkanzi.today.ui.screens.search.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arkanzi.today.repository.NoteRepository
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.screens.search.SearchViewModel
import com.arkanzi.today.ui.screens.search.SearchViewmodelFactory

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchTopBar(
    backStack: NavBackStack<NavKey>,
    animationScope: AnimatedVisibilityScope?,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: SearchViewModel
) {
    with(sharedTransitionScope) {
        TopAppBarCustom(
            fullWidth = true,
            fullWidthContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val newModifier: Modifier = if (animationScope != null) {
                        Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "search_bar"),
                                animatedVisibilityScope = animationScope,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                            )
                    } else {
                        Modifier
                    }
                    CustomSearchBar(
                        modifier = newModifier,
                        onSearchQueryChange = {
                            viewModel.updateQuery(
                                it,
                                System.currentTimeMillis()
                            )
                        },
                        backAction = { backStack.removeLastOrNull() })
                }
            }
        )
    }
}