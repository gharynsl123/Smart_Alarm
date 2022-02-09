package com.example.smartalarm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.armand.smartalarm.helper.TAG_TIME_PICKER
import com.armand.smartalarm.helper.timeFormatter
import com.example.smartalarm.data.Alarm
import com.example.smartalarm.data.local.AlarmDB
import com.example.smartalarm.data.local.AlarmDao
import com.example.smartalarm.databinding.ActivityOneTimeAlarmBinding
import com.example.smartalarm.fragment.DatePickerFragment
import com.example.smartalarm.fragment.TimePickerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OneTimeAlarmActivity : AppCompatActivity(), DatePickerFragment.DateDialogListener,
    TimePickerFragment.TimeDialogListener {

    private var _binding: ActivityOneTimeAlarmBinding? = null
    private val binding get() = _binding as ActivityOneTimeAlarmBinding

    private lateinit var alarmDao: AlarmDao
    private var _alarmService: AlarmService? = null
    private val alarmService get() = _alarmService as AlarmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOneTimeAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(this)
        alarmDao = db.alarmDao()

        _alarmService = AlarmService()

        initView()
    }

    private fun initView() {
        binding.apply {

            btnSetDateOneTime.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, "DatePickerDialog")
            }


            btnSetTimeOneTime.setOnClickListener {
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TAG_TIME_PICKER)
            }

            btnAddSetOneTimeAlarm.setOnClickListener {
                val date = tvOneDate.text.toString()
                val time = tvOneTime.text.toString()
                val message = tvNoteOneTime.text.toString()

                if (date != "Date" && time != "Time") {
                    alarmService.setOneTimeAlarm(applicationContext, 1, date, time, message)
                    CoroutineScope(Dispatchers.IO).launch {
                        alarmDao.addAlarm(Alarm(0, date, time, message, AlarmService.TYPE_ONE_TIME))
                    }
                    Log.i("AddAlarm", "Success set alarm on $date $time with massage $message")
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Set Your Date & Time", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            btnCancelSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(this@OneTimeAlarmActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.tvOneDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        binding.tvOneTime.text = timeFormatter(hourOfDay, minute)
    }

    //Close the Activity
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
