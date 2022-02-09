package com.example.smartalarm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartalarm.adapter.AlarmAdapter
import com.example.smartalarm.data.local.AlarmDB
import com.example.smartalarm.data.local.AlarmDao
import com.example.smartalarm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private var _bindin: ActivityMainBinding? = null
    private val binding get() = _bindin as ActivityMainBinding


    private var alarmDao: AlarmDao? = null
    private var alarmAdapter: AlarmAdapter? = null

    private var alarmService: AlarmService? = null

    override fun onResume() {
        super.onResume()
        alarmDao?.getAlarm()?.observe(this) {
            alarmAdapter?.setData(it)
            Log.i("GetAlarm", "getAlarm : Alarm With $it")
        }
        /*
        CoroutineScope(Dispatchers.IO).launch {
            val alarm = alarmDao?.getAlarm()
            withContext(Dispatchers.Main){
                alarm?.let { alarmAdapter?.setData(it) }
            }
        }
        */
    }

    //Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bindin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AlarmDB.getDatabase(applicationContext)
        alarmDao = db.alarmDao()

        alarmAdapter = AlarmAdapter()

        alarmService = AlarmService()

        initView()
        setupRecyclerView()

    }

    //Set Up RecyclerView
    private fun setupRecyclerView() {
        binding.rvReminderAlarm.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alarmAdapter
            swipeToDelete(this)
        }
    }

    //Function init View
    private fun initView() {
        binding.apply {
            viewSetOneTimeAlarm.setOnClickListener {
                startActivity(Intent(applicationContext, OneTimeAlarmActivity::class.java))
            }

            viewSetRepeatingAlarm.setOnClickListener {
                startActivity(Intent(this@MainActivity, RepeatingAlarmActivity::class.java))
                finish()
            }
        }
    }

    //Delete Function
    private fun swipeToDelete(recyclerView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedAlarm = alarmAdapter?.listAlarm?.get(viewHolder.adapterPosition)
                CoroutineScope(Dispatchers.IO).launch {
                    deletedAlarm?.let { alarmDao?.deleteAlarm(it) }
                    Log.i("DeleteAlarm", "onSwiped: deletAlarm $deletedAlarm")
                }
                val alarmType = deletedAlarm?.type
                alarmType?.let { alarmService?.cencelAlarm(baseContext, it) }
            }

        }).attachToRecyclerView(recyclerView)
    }
}