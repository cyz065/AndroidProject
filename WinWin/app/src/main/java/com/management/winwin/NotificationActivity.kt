package com.management.winwin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.management.winwin.databinding.ActivityNotificationBinding
import com.management.winwin.noti.Approve
import com.management.winwin.noti.ComplexRecyclerViewAdapter
import com.management.winwin.noti.Invite

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNotificationBinding
    private val list = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setActionBar()
        //bindDataToAdapter()
        getArrayList()
    }

    fun bindDataToAdapter() {
        binding.notificationList.adapter = ComplexRecyclerViewAdapter(this, getArrayList())
    }

    private fun getArrayList():ArrayList<Any> {
        list.add(Approve("승인 완료", "근무지: GS25 건대사거리점", "승인일시: 09:00 ~ 21:00",
            "출근시각: 09:00", "퇴근시각: 21:00", "적용시급: 50000원", "휴게시간: 5시간", "야간수당: X", "휴일수당: X", "연장수당: X", "총 근무시간: 12시간 00분", "예상급여: 1000000원"))
        list.add(Invite("초대 완료", "근무지: 카페", "코드: 999999", "담당자: 옥수수"))
        list.add(Invite("초대 완료", "근무지: 편의점", "코드: 123456", "담당자: 감자"))
        list.add(Invite("초대 완료", "근무지: 학원", "코드: 365321", "담당자: 오이"))
        binding.notificationList.adapter = ComplexRecyclerViewAdapter(this, list)
        return list
    }


    private fun setActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return false
        }
    }
}