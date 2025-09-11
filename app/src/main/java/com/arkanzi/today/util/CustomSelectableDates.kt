package com.arkanzi.today.util

import androidx.compose.material3.ExperimentalMaterial3Api
import java.time.Instant
import java.time.ZoneOffset
import androidx.compose.material3.SelectableDates
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
class CustomSelectableDates(
    private val startDateMillis: Long = LocalDate.now()
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
) : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= startDateMillis
    }

    override fun isSelectableYear(year: Int): Boolean {
        val startYear = Instant.ofEpochMilli(startDateMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .year
        return year >= startYear
    }
}