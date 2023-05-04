package com.management.winwin.Card

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.management.winwin.R
import com.management.winwin.databinding.InfoItemBinding
import java.text.DecimalFormat

class WorkInfoAdapter(val context: Context, private val workList:ArrayList<Work>) : RecyclerView.Adapter<WorkInfoAdapter.ViewHolder>(){
    private val decimalFormat = DecimalFormat("#,###")
    private val colors = context.resources.getIntArray(R.array.cardColors)
    private var index = 0


    inner class ViewHolder(val binding: InfoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info:Work) {
            binding.workSite.text = info.workSite
            val moneyFormat = decimalFormat.format(info.money.toDouble())
            val sb = StringBuilder()
            sb.append("\u20A9").append(moneyFormat.toString())
            binding.money.text = sb.toString()
            index %= colors.size
            binding.cardView.setCardBackgroundColor(colors[index])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        index = holder.adapterPosition
        holder.bind(workList[position])
    }

    override fun getItemCount(): Int = workList.size
}