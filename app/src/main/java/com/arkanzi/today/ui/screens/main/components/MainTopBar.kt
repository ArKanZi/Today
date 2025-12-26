package com.arkanzi.today.ui.screens.main.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arkanzi.today.R
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.navigation.SearchNotesScreenKey

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainTopBar(
    backStack: NavBackStack<NavKey>,
    animationScope: AnimatedVisibilityScope?,
    sharedTransitionScope: SharedTransitionScope,
) {
    with(sharedTransitionScope) {
        val newModifier: Modifier = if (animationScope != null) {
            Modifier
                .size(32.dp)
                .padding(1.dp)
                .shadow(
                    1.dp,
                    shape = CircleShape,
                    ambientColor = DefaultShadowColor,
                    spotColor = DefaultShadowColor
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceBright)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "search_bar"),
                    animatedVisibilityScope = animationScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                )
                .clickable {
                    backStack.add(SearchNotesScreenKey)
                }
        } else {
            Modifier
        }
        TopAppBarCustom(
            fullWidth = false,
            rightContent = {
                // 🔹 Shared transition search icon
                Box(
                    modifier = newModifier,
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
            }
        )
    }
}