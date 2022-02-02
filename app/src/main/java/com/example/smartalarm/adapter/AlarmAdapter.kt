package com.example.smartalarm.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.smartalarm.data.Alarm
import com.example.smartalarm.databinding.RowItemAlarmBinding

class AlarmAdapter() :
    RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {
    var listAlarm: ArrayList<Alarm> = arrayListOf()
    inner class MyViewHolder(val binding: RowItemAlarmBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val alarm = listAlarm[position]
        holder.binding.apply {
            itemDateAlarm.text = alarm.date
            itemTimeAlarm.text = alarm.time
            itemNoteAlarm.text = alarm.message
        }
    }

    override fun getItemCount() = listAlarm.size

    fun setData(list: List<Alarm>){
        val alarmDiffUtil = AlarmDiffUtil(listAlarm, list)
        val alarmDiffUtilResult= DiffUtil.calculateDiff(alarmDiffUtil)
        this.listAlarm = list as ArrayList<Alarm>
        alarmDiffUtilResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}