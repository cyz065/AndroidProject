package com.management.winwin.Calendar

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.management.winwin.R
import com.management.winwin.databinding.InfoListBinding
import com.management.winwin.databinding.ItemBinding
import com.management.winwin.startServer.WorkDetail
import java.lang.StringBuilder
import java.text.DecimalFormat

class DetailInfoAdapter(val context: Context, private val infoList:ArrayList<DetailInfo>) : RecyclerView.Adapter<DetailInfoAdapter.ViewHolder>() {
    private val decimalFormat = DecimalFormat("#,###")
    private var index = 0

    inner class ViewHolder(val binding: InfoListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info:DetailInfo) {
            binding.schedule.text = info.date
            binding.workTime.text = "근무시간: " + info.workTime
            binding.restTime.text = timeCalc(info.breakTime)//info.breakTime.toString() + "분"

            val moneyFormat = decimalFormat.format(info.wage.toDouble())
            val totalFormat = decimalFormat.format(info.totalPay.toDouble())
            binding.money.text = "시급: " + moneyFormat.toString() + "원"
            binding.total.text = totalFormat.toString() + "원"

            binding.detail.setOnClickListener {
                if(binding.hidenView.visibility == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                    binding.hidenView.visibility = View.GONE
                    binding.detail.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                }
                else {
                    TransitionManager.beginDelayedTransition(binding.cardView, AutoTransition())
                    binding.hidenView.visibility = View.VISIBLE
                    binding.detail.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                }
            }
            /*
            binding.detail.setOnClickListener {
                val show = toggleLayout(!info.isExpand, it, binding.layoutExpand)
                info.isExpand = show
            }*/
        }

        private fun toggleLayout(isExpand:Boolean, view:View, layoutExpand:LinearLayoutCompat):Boolean {
            ToggleAnimation.toggleArrow(view, isExpand)
            Log.e("토글 여부", "$isExpand")
            if(isExpand) ToggleAnimation.expand(layoutExpand)
            else ToggleAnimation.collapse(layoutExpand)
            return isExpand
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InfoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        index = holder.adapterPosition
        holder.bind(infoList[position])
    }

    override fun getItemCount(): Int = infoList.size

    private fun timeCalc(time:Int):String {
        val sb = StringBuilder()
        sb.append("휴게시간: ")
        var hour = 0
        var min = 0

        hour = (time / 60)
        min = (time % 60)

        if(hour > 0) sb.append(hour).append("시간").append(min).append("분")
        else sb.append(min).append("분")

        return sb.toString()
    }
}