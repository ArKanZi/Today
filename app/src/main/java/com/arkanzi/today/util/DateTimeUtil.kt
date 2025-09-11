package com.arkanzi.today.util

import android.text.format.DateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

fun displayTime(time: Long): String {
    return DateFormat.format("hh:mm a", Date(time)).toString()
}
fun displayDate(date: Long): String {
    return DateFormat.format("dd/MM/yyyy", Date(date)).toString()
}

fun combineDateAndTime(dateMillis: Long, timeMillis: Long): Long {
    val dateCalendar = Calendar.getInstance().apply { timeInMillis = dateMillis }
    val timeCalendar = Calendar.getInstance().apply { timeInMillis = timeMillis }

    return Calendar.getInstance().apply {
        // Set date components from dateMillis
        set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR))
        set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH))

        // Set time components from timeMillis
        set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND))
        set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND))
    }.timeInMillis
}

// Usage

