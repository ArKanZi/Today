package com.arkanzi.today.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ReminderDebug", "ReminderReceiver triggered!")
        val title = intent.getStringExtra("title") ?: "Reminder"
        val message = intent.getStringExtra("message") ?: "You have a task."

        NotificationUtils.createNotificationChannel(context)
        NotificationHelper.showNotification(context, title, message)
    }
}
