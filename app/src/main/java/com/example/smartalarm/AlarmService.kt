package com.example.smartalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.util.*

//Creating Service For Notivication
class AlarmService {
    fun setOneTimeAlarm(context: Context, type: Int, date: String, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //Put Data Intent
        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra("message", message)
        intent.putExtra("type", type)

        //Convertig
        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        //Date
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]))
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
        //Time
        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        //Pending Intent
        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Success seet OnetimAlarm", Toast.LENGTH_SHORT).show()
    }
}