package com.example.smartalarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

//Creating Service For Notification
class AlarmService : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        val type = intent?.getIntExtra(EXTRA_TYPE, 0)

        val title = when (type) {
            TYPE_ONE_TIME -> "One Time Alarm"
            TYPE_REPEATING -> "Repeating Alarm"
            else -> "Something Wrong Here."
        }

        if (context != null && message != null) {
            val requestCode = when (type) {
                TYPE_ONE_TIME -> NOTIF_ID_ONE_TIME
                TYPE_REPEATING -> NOTIF_ID_REPEATING
                else -> -1
            }
            showNotificationAlarm(
                context,
                title,
                message,
                requestCode
            )
        }
    }

    fun cencelAlarm(context: Context, type: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmService::class.java)
        val requestCode = when (type) {
            TYPE_ONE_TIME -> NOTIF_ID_ONE_TIME
            TYPE_REPEATING -> NOTIF_ID_REPEATING
            else -> Log.d("CencelAlarm", "Unknown Type Of Alarm")
        }
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        if (type == TYPE_ONE_TIME) {
            Toast.makeText(context, "One Time Alarm Canceled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Repeating Alarm Canceled", Toast.LENGTH_SHORT).show()
        }

    }

    //set Repeating Alarm
    fun setRepeatingAlarm(context: Context, type: Int, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra("message", message)
        intent.putExtra("type", type)

        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()

        //Time
        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        //Pending Intent
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_REPEATING, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Success set RepeatingAlarm", Toast.LENGTH_SHORT).show()
    }

    //set One Time Alarm
    fun setOneTimeAlarm(context: Context, type: Int, date: String, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //Put Data Intent
        val intent = Intent(context, AlarmService::class.java)
        intent.putExtra("message", message)
        intent.putExtra("type", type)

        //Converting
        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        //Date
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
        //Time
        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        //Pending Intent
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_ONE_TIME, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Success set OnetimeAlarm", Toast.LENGTH_SHORT).show()
    }

    private fun showNotificationAlarm(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val channelName = "SmartAlarm"
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val channelId = "smart_alarm"

        val notificationManage =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_one_time)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(ringtone)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManage.createNotificationChannel(channel)
        }
        val notif = builder.build()
        notificationManage.notify(notificationId, notif)
    }

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val NOTIF_ID_ONE_TIME = 101
        const val NOTIF_ID_REPEATING = 102

        const val TYPE_ONE_TIME = 1
        const val TYPE_REPEATING = 0
    }
}