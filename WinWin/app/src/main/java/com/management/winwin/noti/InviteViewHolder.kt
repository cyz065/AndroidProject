package com.management.winwin.noti

import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.management.winwin.databinding.InviteItemBinding

class InviteViewHolder(val binding:InviteItemBinding): RecyclerView.ViewHolder(binding.root) {
    var workSite = binding.workSite
    var code = binding.code
    var manager = binding.manager
    var checkBtn = binding.checkBtn
    var message = binding.title
}