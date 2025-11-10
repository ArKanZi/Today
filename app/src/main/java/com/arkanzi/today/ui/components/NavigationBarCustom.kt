package com.arkanzi.today.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.arkanzi.today.R
import com.arkanzi.today.ui.navigation.AddNotesKey
import com.arkanzi.today.ui.navigation.CalendarScreenKey
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.ui.navigation.SettingsScreenKey
import com.arkanzi.today.ui.navigation.StatsScreenKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NavigationBarCustom(backStack: NavBackStack<NavKey>) {
    val scope = rememberCoroutineScope()
    val top = backStack.lastOrNull()
    val selectedTint = MaterialTheme.colorScheme.onSurfaceVariant
    val unselectedTint = MaterialTheme.colorScheme.outline
    fun tintFor(key: Any) = if (top == key) selectedTint else unselectedTint
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .navigationBarsPadding()
    ) {
        // The nav bar row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                )
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

//            Home button
            IconButton(
                onClick = {
                    if (backStack.lastOrNull() != MainScreenKey)
                        scope.launch {
                            backStack.add(MainScreenKey)
                        }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = tintFor(MainScreenKey),
                    modifier = Modifier.size(24.dp)
                )
            }

//            Stats button
            IconButton(
                onClick = {
                    if (backStack.lastOrNull() != StatsScreenKey)
                        scope.launch {
                            backStack.add(StatsScreenKey)
                        }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_activity),
                    contentDescription = "Notes",
                    tint = tintFor(StatsScreenKey),
                    modifier = Modifier.weight(1f)
                )
            }
//            Add Note Button
            Box(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF2EB4C8),
                                    Color(0xFF5CE487)
                                )
                            ),
                            shape = CircleShape
                        )
                        .clickable { backStack.add(AddNotesKey) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cross),
                        contentDescription = "Add To Do",
                        tint = Color.White,
                        modifier = Modifier
                            .rotate(45f)
                            .size(24.dp)

                    )
                }
            }

//            Calendar Button
            IconButton(
                onClick = {
                    if (backStack.lastOrNull() != CalendarScreenKey)
                        scope.launch { backStack.add(CalendarScreenKey) }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    tint = tintFor(CalendarScreenKey),
                    modifier = Modifier.weight(1f)
                )
            }

//            Settings Button
            IconButton(
                onClick = {
                    if (backStack.lastOrNull() != SettingsScreenKey)
                        scope.launch {backStack.add(SettingsScreenKey) }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cogwheel),
                    contentDescription = "Settings",
                    tint = tintFor(SettingsScreenKey),
                    modifier = Modifier.weight(1f)
                )
            }
        }

    }

}

@Preview
@Composable
fun NavigationBarCustomPreview() {
    val backStack = rememberNavBackStack(MainScreenKey)
    NavigationBarCustom(backStack)
}
