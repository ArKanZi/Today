package com.arkanzi.today.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.room.util.TableInfo
import com.arkanzi.today.R
import com.arkanzi.today.ui.navigation.AddNotesKey
import com.arkanzi.today.ui.navigation.MainScreenKey

@Composable
fun NavigationBarCustom(backStack: NavBackStack) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        // The nav bar row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    color = Color.White,
                )
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_activity),
                    contentDescription = "Notes",
                    tint = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }
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
                    ).clickable{ backStack.add(AddNotesKey(screenId = "11")) },
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

            IconButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    tint = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cogwheel),
                    contentDescription = "Settings",
                    tint = Color.Gray,
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
