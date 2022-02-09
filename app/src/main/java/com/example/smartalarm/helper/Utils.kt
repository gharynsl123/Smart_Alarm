package com.armand.smartalarm.helper

import java.text.SimpleDateFormat
import java.util.*

const val TAG_TIME_PICKER = "TimePickerDialog"

fun timeFormatter(hourOfDay: Int, minute: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(0, 0, 0, hourOfDay, minute)
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
}