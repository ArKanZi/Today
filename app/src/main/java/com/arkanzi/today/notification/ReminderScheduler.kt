package com.arkanzi.today.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

object ReminderScheduler {

    /**
     * Schedules a reminder at a specific timestamp.
     */
    fun scheduleReminder(
        context: Context,
        requestId: Int,
        timeInMillis: Long,
        title: String,
        message: String
    ) {
        Log.d("ReminderDebug", "Scheduling reminder for: $timeInMillis")

        if (timeInMillis <= System.currentTimeMillis()) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Cannot set reminder in the past", Toast.LENGTH_SHORT).show()
            }
            return  // Prevent scheduling past alarms
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestId, // unique ID
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    /**
     * Cancels a previously scheduled reminder.
     */
    fun cancelReminder(
        context: Context,
        requestId: Int  // noteId or time.hashCode()
    ) {
        val intent = Intent(context, ReminderReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel pending intent + alarm
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}
