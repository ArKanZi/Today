package com.arkanzi.today.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserPreferences private constructor(context: Context) {
    companion object {
        private const val PREF_NAME = "today_app_prefs"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_IS_DARK_THEME = "is_dark_theme"

        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferences(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _isDarkTheme = MutableStateFlow(getCurrentThemeFromPrefs())
    val isDarkThemeFlow: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private fun getCurrentThemeFromPrefs(): Boolean {
        return sharedPrefs.getBoolean(KEY_IS_DARK_THEME, false)
    }

    fun saveUserName(name: String) {
        sharedPrefs.edit { putString(KEY_USER_NAME, name) }
    }

    fun getUserName(): String {
        return sharedPrefs.getString(KEY_USER_NAME, "User") ?: "User"
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.update {
            sharedPrefs.edit { putBoolean(KEY_IS_DARK_THEME, isDark) }
            isDark
        }
    }

    fun toggleTheme() {
        _isDarkTheme.update { currentValue ->
            val newValue = !currentValue
            sharedPrefs.edit { putBoolean(KEY_IS_DARK_THEME, newValue) }
            newValue
        }
    }
}
