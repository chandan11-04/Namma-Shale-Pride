package com.nammashale.shalepride.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private const val DATE_FORMAT_DISPLAY = "dd MMM yyyy"
    private const val DATE_FORMAT_DB = "yyyy-MM-dd"
    private const val DATE_TIME_FORMAT = "dd MMM yyyy, hh:mm a"

    fun getTodayDateString(): String {
        return SimpleDateFormat(DATE_FORMAT_DB, Locale.getDefault()).format(Date())
    }

    fun formatForDisplay(date: Date): String {
        return SimpleDateFormat(DATE_FORMAT_DISPLAY, Locale.getDefault()).format(date)
    }

    fun formatForDisplay(dateString: String): String {
        return try {
            val date = SimpleDateFormat(DATE_FORMAT_DB, Locale.getDefault()).parse(dateString)
            date?.let { formatForDisplay(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    fun formatDateTime(timestamp: Long): String {
        return SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(Date(timestamp))
    }

    fun getRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Just now"
            diff < 3_600_000 -> "${diff / 60_000}m ago"
            diff < 86_400_000 -> "${diff / 3_600_000}h ago"
            diff < 604_800_000 -> "${diff / 86_400_000}d ago"
            else -> formatDateTime(timestamp)
        }
    }

    fun isSameDay(timestamp: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp }
        val cal2 = Calendar.getInstance()
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}
