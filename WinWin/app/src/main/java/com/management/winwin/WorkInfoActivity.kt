package com.management.winwin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.management.winwin.Card.CardAdapter
import com.management.winwin.Card.Work
import com.management.winwin.Card.WorkInfoAdapter
import com.management.winwin.databinding.ActivityWorkInfoBinding
import com.management.winwin.preference.Application
import com.management.winwin.startServer.ResponseSimpleWork
import com.management.winwin.startServer.RetrofitAPI
import com.management.winwin.startServer.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class WorkInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWorkInfoBinding
    private val workList = ArrayList<Work>()
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkInfoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setActionBar()

        val startSearchTime = intent.getStringExtra("Start") ?: LocalDateTime.now().toString()
        val endSearchTime = intent.getStringExtra("End") ?: LocalDateTime.now().toString()

        getWorkInfo(startSearchTime, endSearchTime)
        //binding.moneyAmount.text = getMoney()
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

    private fun getWorkInfo(startSearchTime:String, endSearchTime:String) {
        val retIn = RetrofitService.getRetrofitInstance().create(RetrofitAPI::class.java)
        val token = Application.prefs.getString("token", "")
        if(token == "")
            return

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
        val start = inputFormat.parse(startSearchTime)
        val end = inputFormat.parse(endSearchTime)

        val startString = dateFormat.format(start)
        val endString = dateFormat.format(end)

        Log.e("확인", "$startString $endString")
        val sb = StringBuilder()
        binding.searchPeriod.text = sb.append(startString).append(" ~ ").append(endString).toString()

        retIn.getSimpleWork(authorization = token, startSearchTime = startSearchTime, endSearchTime = endSearchTime).enqueue(object :
            Callback<ResponseSimpleWork> {
            override fun onResponse(
                call: Call<ResponseSimpleWork>,
                response: Response<ResponseSimpleWork>
            ) {
                if(response.isSuccessful) {
                    val body = response.body()
                    val code = body!!.code
                    val message = body.message
                    val data = body.data
                    var totalMoney = 0.0
                    for(store in data) {
                        totalMoney += store.totalPay.toDouble()
                        workList.add(store)
                    }
                    binding.moneyAmount.text = decimalFormat.format(totalMoney) + "원"
                    val adapter = WorkInfoAdapter(baseContext, workList)
                    binding.workInfoRecyclerView.adapter = adapter
                }
                else {
                    Log.e("랭킹", "실패 ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseSimpleWork>, t: Throwable) {
                Log.e("랭킹", "완전 실패 ${t.message}")
            }
        })


        /*
        workList.add(Work("GS25 강남점", "184567", "03.01 ~ 03.31", "270000"))
        workList.add(Work("CU 강남점", "172459", "02.01 ~ 02.28", "300000"))
        workList.add(Work("스타벅스 강남점", "987654", "02.18 ~ 02.25", "100000"))
        workList.add(Work("메가커피 강남점", "487234", "01.18 ~ 01.31", "200000"))
        workList.add(Work("이디야 커피 강남점", "165832", "03.01 ~ 04.19", "450000"))
        workList.add(Work("CGV 강남점", "125345", "04.01 ~ 04.03", "25000"))
        workList.add(Work("메가박스 강남점", "029863", "05.01 ~ 05.05", "10000"))
        workList.add(Work("롯데리아 강남점", "472943", "04.19 ~ 04.26", "23000"))*/
    }

    private fun getMoney(): String {
        var total = 0.0

        if (workList.size != 0) {
            for (work in workList) {
                total += work.totalPay.toDouble()
            }
        }
        return decimalFormat.format(total) + "원"
    }
}