package com.arkanzi.today.notification

import android.Manifest
import android.content.pm.PackageManager
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arkanzi.today.R

object NotificationHelper {

    private const val NOTIFICATION_ID = 1001

    /**
     * Shows a simple notification
     */
    fun showNotification(context: Context, title: String, message: String) {

        // Android 13+ requires POST_NOTIFICATIONS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return // Don't crash — simply exit if permission is not granted
            }
        }

        val builder = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // <- Add an icon in your drawable
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)   // REQUIRED
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}

