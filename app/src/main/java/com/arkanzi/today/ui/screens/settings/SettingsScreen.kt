package com.arkanzi.today.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arkanzi.today.R
import com.arkanzi.today.ui.components.IconContainer
import com.arkanzi.today.ui.components.InputFields
import com.arkanzi.today.util.UserPreferences

@Composable
fun SettingsScreen(
    userPreferences: UserPreferences
) {
    val viewModel: SettingsViewmodel = viewModel(
        factory = SettingsViewModelFactory(userPreferences)
    )
    val isDarkTheme by userPreferences.isDarkThemeFlow.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxSize()){
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
                    onCheckedChange = { newvalue ->
                        userPreferences.setDarkTheme(newvalue)
                    },
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
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier.weight(3f)) {
                InputFields(
                    value = viewModel.userName,
                    onValueChange = viewModel::onUserNameChange, // Use ViewModel function
                    icon = R.drawable.ic_user,
                    description = "",
                    label = "User Name",
                    tint = Color(0xFF2F70E1),
                    singleLine = true,
                    maxLength = 50,
                )
            }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End){
                Button(
                    onClick = { viewModel.saveUserName() }
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences.getInstance(context) }
    SettingsScreen(userPrefs)
}