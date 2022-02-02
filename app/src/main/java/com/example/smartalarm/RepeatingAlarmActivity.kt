package com.example.smartalarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartalarm.data.local.AlarmDB
import com.example.smartalarm.data.local.AlarmDao
import com.example.smartalarm.databinding.ActivityRepeatingAlarmBinding

class RepeatingAlarmActivity : AppCompatActivity() {

    private var _binding : ActivityRepeatingAlarmBinding? = null
    private val binding get() = _binding as ActivityRepeatingAlarmBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRepeatingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}