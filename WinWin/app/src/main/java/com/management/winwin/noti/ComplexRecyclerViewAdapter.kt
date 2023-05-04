package com.management.winwin.noti

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.management.winwin.R
import com.management.winwin.databinding.ApproveItemBinding
import com.management.winwin.databinding.InviteItemBinding
import com.management.winwin.databinding.NotiErrorBinding

class ComplexRecyclerViewAdapter(val context:Context, val items:ArrayList<Any>) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val INVITE = 0
    private val APPROVE = 1
    private lateinit var bindingInvite:InviteItemBinding
    private lateinit var bindingApprove:ApproveItemBinding
    private lateinit var bindingError:NotiErrorBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder:RecyclerView.ViewHolder
        //val inflater = LayoutInflater.from(parent.context)

        bindingInvite = InviteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        bindingApprove = ApproveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        when(viewType) {
            INVITE -> {
                viewHolder = InviteViewHolder(bindingInvite)
            }
            APPROVE -> {
                viewHolder = ApproveViewHolder(bindingApprove)
            }
            else -> {
                //val v:View = inflater.inflate(R.layout.noti_error, parent, false)
                //val view:View = ErrorViewHolder(bindingError)
                viewHolder = ErrorViewHolder(bindingError)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            INVITE-> {
                Log.e("바인딩", "바인딩 invite")
                val viewHolder = holder as InviteViewHolder
                configureViewHolderInvite(viewHolderInvite = viewHolder, position)
            }
            APPROVE -> {
                Log.e("바인딩", "바인딩 approve")
                val viewHolder = holder as ApproveViewHolder
                configureViewHolderApprove(viewHolderApprove = viewHolder, position)
            }
        }
    }

    private fun configureViewHolderInvite(viewHolderInvite:InviteViewHolder, position: Int) {
        val item = items[position] as Invite
        bindingInvite.title.text = item.message
        bindingInvite.workSite.text = item.workSite
        bindingInvite.code.text = item.code
        bindingInvite.manager.text = item.manager
    }

    private fun configureViewHolderApprove(viewHolderApprove:ApproveViewHolder, position:Int) {
        val item = items[position] as Approve
        bindingApprove.message.text = item.message
        bindingApprove.workSite.text = item.workSite
        bindingApprove.timeStamp.text = item.timeStamp
        bindingApprove.startTime.text = item.startTime
        bindingApprove.endTime.text = item.endTime
        bindingApprove.applyWage.text = item.applyWage
        bindingApprove.restTime.text = item.restTime
        bindingApprove.night.text = item.night
        bindingApprove.holiday.text = item.holiday
        bindingApprove.over.text = item.over
        bindingApprove.totalTime.text = item.totalTime
        bindingApprove.totalWage.text = item.totalWage
    }

    override fun getItemCount(): Int {
        Log.e("바인딩", "${items.size}")
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if(items[position] is Invite) {
            return INVITE
        }
        else if(items[position] is Approve) {
            return APPROVE
        }
        return -1
    }
}