package com.management.winwin.noti

import androidx.recyclerview.widget.RecyclerView
import com.management.winwin.databinding.ApproveItemBinding

class ApproveViewHolder(val binding:ApproveItemBinding):RecyclerView.ViewHolder(binding.root) {
    val message = binding.message
    val workSite = binding.workSite
    val timeStamp = binding.timeStamp
    val startTime = binding.startTime
    val endTime = binding.endTime
    val applyWage = binding.applyWage
    val restTime = binding.restTime
    val night = binding.night
    val holiday = binding.holiday
    val over = binding.over
    val totalTime = binding.totalTime
    val totalWage = binding.totalWage
    val checkBtn = binding.checkBtn

}