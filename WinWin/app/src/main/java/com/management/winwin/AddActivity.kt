package com.management.winwin

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.management.winwin.databinding.ActivityAddBinding
import com.management.winwin.preference.Application
import com.management.winwin.startServer.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.util.*

class AddActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityAddBinding
    private val decimalFormat = DecimalFormat("#,###")
    private var money = ""
    private val percentage = Array(41) {index -> (index * 5).toString()}
    private var storeId = -1

    private var workYear:Int = 0
    private var workMonth:Int = 0
    private var workDay:Int = 0
    private var workStartHour:Int = 0
    private var workStartMin:Int = 0
    private var workEndHour:Int = 0
    private var workEndMin:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        storeId = intent.getIntExtra("StoreId", -1)
        binding.inputDate.setOnClickListener(this)
        binding.startTimePicker.setOnClickListener(this)
        binding.endTimePicker.setOnClickListener(this)

        binding.payInput.addTextChangedListener(object:TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.toString() != money) {
                    money = decimalFormat.format((s.toString().replace(",", "")).toLong())
                    binding.payInput.setText(money)
                    binding.payInput.setSelection(money.length)
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.holidayPay.setOnClickListener(this)
        binding.nightPay.setOnClickListener(this)
        binding.overPay.setOnClickListener(this)
        binding.requestBtn.setOnClickListener(this)
    }

    private fun setActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.inputDate -> {
                val day = Calendar.getInstance()
                val year = day.get(Calendar.YEAR)
                val month = day.get(Calendar.MONTH)
                val date = day.get(Calendar.DATE)

                val datePickerDialog =
                    DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                        workYear = year
                        workMonth = monthOfYear
                        workDay = dayOfMonth

                        val m = String.format("%02d-", (monthOfYear + 1))
                        val d = String.format("%02d", dayOfMonth)
                        val sb = StringBuilder()
                        sb.append(year).append("-").append(m).append(d)
                        binding.inputDate.setText(sb.toString())
                    }, year, month, date)
                datePickerDialog.show()
            }

            R.id.startTimePicker-> {
                val timePickerDialog = TimePickerDialog(this , { _, hour, min ->
                    workStartHour = hour
                    workStartMin = min
                    val h = String.format("%02d", hour)
                    val m = String.format("%02d", min)
                    val sb = StringBuilder()
                    sb.append(h).append(" : ").append(m)
                    binding.startTimePicker.setText(sb.toString()) }, 0, 0, false)
                timePickerDialog.show()
            }

            R.id.endTimePicker-> {
                val timePickerDialog = TimePickerDialog(this , { _, hour, min ->
                    workEndHour = hour
                    workEndMin = min

                    val h = String.format("%02d", hour)
                    val m = String.format("%02d", min)
                    val sb = StringBuilder()
                    sb.append(h).append(" : ").append(m)
                    binding.endTimePicker.setText(sb.toString()) }, 0, 0, false)
                timePickerDialog.show()
            }

            R.id.holidayPay-> {
                val layout = layoutInflater.inflate(R.layout.number_picker, null)
                val numberPicker = layout.findViewById<NumberPicker>(R.id.numberPicker)
                val title = layout.findViewById<AppCompatTextView>(R.id.text)
                title.text = "휴일수당 선택"
                numberPicker.maxValue = percentage.size - 1
                numberPicker.minValue = 0
                numberPicker.value = 20
                Log.e("percentage", percentage.contentToString())
                numberPicker.displayedValues = percentage
                numberPicker.wrapSelectorWheel = false

                val builder = AlertDialog.Builder(this)
                builder.setView(layout)
                    .setPositiveButton("OK"
                    ) { dialog, _ ->
                        val selectedValue = numberPicker.value * 5
                        binding.holidayPay.text = "$selectedValue%"
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel"
                    ) { dialog, _ -> dialog.dismiss() }
                val dialog = builder.create()
                dialog.show()
            }

            R.id.overPay-> {
                val layout = layoutInflater.inflate(R.layout.number_picker, null)
                val numberPicker = layout.findViewById<NumberPicker>(R.id.numberPicker)
                val title = layout.findViewById<AppCompatTextView>(R.id.text)
                title.text = "연장수당 선택"
                numberPicker.maxValue = percentage.size - 1
                numberPicker.minValue = 0
                numberPicker.value = 20
                numberPicker.displayedValues = percentage
                numberPicker.wrapSelectorWheel = false
                val builder = AlertDialog.Builder(this)
                builder.setView(layout)
                    .setPositiveButton("OK"
                    ) { dialog, _ ->
                        val selectedValue = numberPicker.value * 5
                        binding.overPay.text = "$selectedValue%"
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel"
                    ) { dialog, _ -> dialog.dismiss() }
                val dialog = builder.create()
                dialog.show()
            }

            R.id.nightPay-> {
                val layout = layoutInflater.inflate(R.layout.number_picker, null)
                val numberPicker = layout.findViewById<NumberPicker>(R.id.numberPicker)
                val title = layout.findViewById<AppCompatTextView>(R.id.text)
                title.text = "야간수당 선택"

                numberPicker.maxValue = percentage.size - 1
                numberPicker.minValue = 0
                numberPicker.value = 20
                numberPicker.displayedValues = percentage
                numberPicker.wrapSelectorWheel = false
                val builder = AlertDialog.Builder(this)
                builder.setView(layout)
                    .setPositiveButton("OK"
                    ) { dialog, _ ->
                        val selectedValue = numberPicker.value * 5
                        binding.nightPay.text = "$selectedValue%"
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel"
                    ) { dialog, _ -> dialog.dismiss() }
                val dialog = builder.create()
                dialog.show()
            }

            R.id.requestBtn -> {
                requestToServer()
            }
        }
    }

    private fun requestToServer() {
        val retIn = RetrofitService.getRetrofitInstance().create(RetrofitAPI::class.java)
        val startTime = LocalDateTime.of(workYear, workMonth + 1, workDay, workStartHour, workStartMin, 0)
        val endTime = LocalDateTime.of(workYear, workMonth + 1, workDay, workEndHour, workEndMin, 0)

        val wage = binding.payInput.text.toString().replace(",", "").toInt()
        val extraPay = binding.overPay.text.toString().replace("%", "").toInt()
        val totalPay = 0
        val breakTime = binding.breakTimeInput.text.toString()
        val holidayPayRate = binding.holidayPay.text.toString().replace("%", "").toInt()
        val longPayRate = binding.overPay.text.toString().replace("%", "").toInt()
        val nightPayRate = binding.nightPay.text.toString().replace("%", "").toInt()
        val memo = binding.memo.text.toString()

        Log.e("시작 시간 확인", startTime.toString())
        Log.e("끝 시간 확인", endTime.toString())
        Log.e("메시지 확인", "wage : $wage extraPay : $extraPay totalPay : $totalPay breakTime: $breakTime holidayPayRate: $holidayPayRate long: $longPayRate nig: $nightPayRate memo: $memo")
        /*
            val storeId:Int,

    val wage:Int,
    val extraPay:Int,
    val totalPay:Int,
    @SerializedName("breaktime")
    val breakTime:Int,
    val holidayPayRate:Int,
    val longPayRate:Int,
    val nightPayRate:Int,
    val memo:String

         */
        /*
        val info = RequestWork()

        retIn.requestWork(info).enqueue(object:
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("서버 통신 실패", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>) {
                if(response.isSuccessful) {

                }

                else {

                }
            }
        })*/
    }
}