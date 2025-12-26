package com.arkanzi.today.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.arkanzi.today.util.UserPreferences


class SettingsViewmodel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    var userName by mutableStateOf("")
        private set

    init {
        userName = userPreferences.getUserName()
    }

    fun onUserNameChange(newUserName: String) {
        userName = newUserName
    }

    fun saveUserName() {
        userPreferences.saveUserName(userName)
    }
}

