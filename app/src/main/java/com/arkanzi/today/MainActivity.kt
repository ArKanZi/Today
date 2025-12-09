package com.arkanzi.today

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.arkanzi.today.notification.NotificationPermission
import com.arkanzi.today.notification.NotificationUtils
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
                if (
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    !NotificationUtils.hasNotificationPermission(this)
                ) {
                    NotificationPermission()
                }

                AppNavHost()
            }
        }
    }
}