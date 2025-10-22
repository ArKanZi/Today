package com.arkanzi.today.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import com.arkanzi.today.R
import com.arkanzi.today.ui.components.IconContainer
import com.arkanzi.today.ui.components.TopAppBarCustom
import com.arkanzi.today.ui.layout.DefaultLayout
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.util.UserPreferences

@Composable
fun SettingsScreen(
    backStack: NavBackStack,
    userPreferences: UserPreferences
){
    val isDarkTheme by userPreferences.isDarkThemeFlow.collectAsStateWithLifecycle()
    DefaultLayout(topBar = {
        TopAppBarCustom(
            leftContent = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_horizontal_menu),
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                )
            },
            rightContent = {
                Box(
                    modifier = Modifier
                        .size(32.dp).padding(1.dp)
                        .shadow(1.dp, shape = CircleShape, ambientColor = DefaultShadowColor, spotColor = DefaultShadowColor)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceBright).clickable{},
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
            })
    }){
        Column(modifier = Modifier.padding(it).padding(horizontal = 18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                IconContainer(
                    icon = R.drawable.ic_notification,
                    description = "",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color(0xFF2FE16A),
                    onClick = {}
                )
                Column(modifier = Modifier.weight(3f)) { Text("Dark Theme") }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = {newvalue ->
                            userPreferences.setDarkTheme(newvalue) },
                        thumbContent = if (isDarkTheme) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        })
                }
            }
        }

    }
}

@Preview
@Composable
fun SettingsScreenPreview(){
    val backStack = rememberNavBackStack(MainScreenKey)
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences.getInstance(context) }
    SettingsScreen(backStack, userPrefs)
}