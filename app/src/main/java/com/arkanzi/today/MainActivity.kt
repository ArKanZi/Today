package com.arkanzi.today

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkanzi.today.ui.navigation.AppNavHost
import com.arkanzi.today.ui.theme.TodayTheme
import com.arkanzi.today.util.UserPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userPrefs = UserPreferences.getInstance(this)
            TodayTheme(userPreferences = userPrefs) {
                AppNavHost()
            }
        }
    }
}