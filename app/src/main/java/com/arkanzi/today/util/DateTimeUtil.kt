package com.arkanzi.today.util

import android.text.format.DateFormat
import java.util.Date

fun displayTime(time: Long): String {
    return DateFormat.format("hh:mm a", Date(time)).toString()
}
fun displayDate(date: Long): String {
    return DateFormat.format("dd/MM/yyyy", Date(date)).toString()
}