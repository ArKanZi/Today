package com.arkanzi.today.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkanzi.today.util.UserPreferences

class SettingsViewModelFactory(
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewmodel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewmodel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
