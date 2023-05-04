package com.management.winwin.Card

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.management.winwin.R
import com.management.winwin.databinding.ItemBinding
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class CardAdapter(val context: Context, private val workList:ArrayList<Work>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    private val decimalFormat = DecimalFormat("#,###")
    private val colors = context.resources.getIntArray(R.array.cardColors)
    private var index = 0

    interface ItemClick{
        fun onClick(view : View, position:Int)
    }

    var itemClick:ItemClick? = null

    inner class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info:Work) {
            binding.workSite.text = info.workSite
            binding.workCode.text = info.code
            binding.date.text = info.date
            val moneyFormat = decimalFormat.format(info.money.toDouble())
            binding.money.text = moneyFormat.toString()
            index %= colors.size
            binding.cardView.setCardBackgroundColor(colors[index])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        index = holder.adapterPosition
        holder.bind(workList[position])
        if(itemClick != null) {
            holder.binding.cardView.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it, position)
            })
        }
    }

    override fun getItemCount(): Int = workList.size
}