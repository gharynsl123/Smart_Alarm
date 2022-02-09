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
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

//Creating Service For Notification
class AlarmService : BroadcastReceiver() {
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
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1])-1)
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
        //Time
        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)
        //Pending Intent
        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Success set OnetimeAlarm", Toast.LENGTH_SHORT).show()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("message")
        intent?.getIntExtra("type", 0)

        if (context != null && message != null) {
            showNotificationAlarm(context, "Oii", message, 101)
        }
    }

    private fun showNotificationAlarm(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val channelName = "SmartAlarm"
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val notificationManage =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, "Alarm_1")
            .setSmallIcon(R.drawable.ic_one_time)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(ringtone)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("alarm_1", channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            notificationManage.createNotificationChannel(channel)
        }
        val notif = builder.build()
        notificationManage.notify(notificationId, notif)
    }
}