package com.arkanzi.today.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arkanzi.today.ui.navigation.MainScreenKey
import com.arkanzi.today.util.UserPreferences

@Composable
fun UsernameSetupScreen(
    backStack: NavBackStack<NavKey>,
    userPreferences: UserPreferences
) {
    var name by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter Your Name", fontSize = 20.sp)

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            placeholder = { Text("Your name") }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank()) {
                    userPreferences.saveUserName(name)
                    backStack.replaceAll { MainScreenKey }
                }
            }
        ) {
            Text("Continue")
        }
    }
}
