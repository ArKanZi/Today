package com.arkanzi.today.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable object MainScreenKey : NavKey
@Serializable data class AddNotesKey(val screenId: String) : NavKey
