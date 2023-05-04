package com.management.winwin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.management.winwin.Card.Work
import com.management.winwin.Card.WorkInfoAdapter
import com.management.winwin.databinding.ActivityWorkInfoBinding
import java.text.DecimalFormat

class WorkInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWorkInfoBinding
    private val workList = ArrayList<Work>()
    private val decimalFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkInfoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setActionBar()
        getWorkInfo()
        binding.moneyAmount.text = getMoney()
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

    private fun getWorkInfo() {
        workList.add(Work("GS25 강남점", "184567", "03.01 ~ 03.31", "270000"))
        workList.add(Work("CU 강남점", "172459", "02.01 ~ 02.28", "300000"))
        workList.add(Work("스타벅스 강남점", "987654", "02.18 ~ 02.25", "100000"))
        workList.add(Work("메가커피 강남점", "487234", "01.18 ~ 01.31", "200000"))
        workList.add(Work("이디야 커피 강남점", "165832", "03.01 ~ 04.19", "450000"))
        workList.add(Work("CGV 강남점", "125345", "04.01 ~ 04.03", "25000"))
        workList.add(Work("메가박스 강남점", "029863", "05.01 ~ 05.05", "10000"))
        workList.add(Work("롯데리아 강남점", "472943", "04.19 ~ 04.26", "23000"))
        val adapter = WorkInfoAdapter(this, workList)
        binding.workInfoRecyclerView.adapter = adapter
    }

    private fun getMoney(): String {
        var total = 0.0

        if (workList.size != 0) {
            for (work in workList) {
                total += work.money.toDouble()
            }
        }
        return decimalFormat.format(total) + "원"
    }
}