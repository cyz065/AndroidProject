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
import com.management.winwin.preference.Application.Companion.prefs
import com.management.winwin.startServer.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.text.DecimalFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.math.abs

class AddActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityAddBinding
    private val decimalFormat = DecimalFormat("#,###")
    private var money = ""
    private val percentage = Array(41) {index -> (index * 5).toString()}
    private var storeId = -1
    private var storeName = ""

    private var workYear:Int = 0
    private var workMonth:Int = 0
    private var workDay:Int = 0
    private var workStartHour:Int = 0
    private var workStartMin:Int = 0
    private var workEndHour:Int = 0
    private var workEndMin:Int = 0
    private val token = prefs.getString("token", "")
    private var sendingData:String = ""

    private var dayOfWeek = mapOf(
        (1 to "(일)"), (2 to "(월)"), (3 to "(화)"), (4 to "(수)"),
        (5 to "(목)"), (6 to "(금)"), (7 to "(토)")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        storeId = intent.getIntExtra("storeId", -1)
        storeName = intent.getStringExtra("storeName").toString()
        Log.e("Add", "$storeId $storeName")

        binding.workSite.text = "근무지: $storeName"
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
                //val intent = Intent(this, CalendarActivity::class.java)
                //intent.putExtra("date", sendingData)
                //startActivity(intent)
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
                        day.set(year, monthOfYear, dayOfMonth)
                        val dayOfWeek = this.dayOfWeek[day.get(Calendar.DAY_OF_WEEK)]
                       // val s = this.dayOfWeek[dayOfWeek]
                        //Log.e("요일", "$s")
                        val sb = StringBuilder()
                        sb.append(year).append("-").append(m).append(d).append(dayOfWeek)
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
        if(storeId < 0) {
            Toast.makeText(this, "근무지 정보가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        val startTime = LocalDateTime.of(workYear, workMonth + 1, workDay, workStartHour, workStartMin, 0)
        val endTime = LocalDateTime.of(workYear, workMonth + 1, workDay, workEndHour, workEndMin, 0)

        val workTime = Duration.between(startTime, endTime).toMinutes()
        val wage = binding.payInput.text.toString().replace(",", "").toInt()
        val workPay = (workTime / 60) * wage

        val breakTime = binding.breakTimeInput.text.toString()
        val holidayPayRate = binding.holidayPay.text.toString().replace("%", "").toInt()
        val longPayRate = binding.overPay.text.toString().replace("%", "").toInt()
        val nightPayRate = binding.nightPay.text.toString().replace("%", "").toInt()
        val extraPay = wage + ((holidayPayRate / 100) + (longPayRate / 100) + (nightPayRate / 100)) * wage
        val totalPay = workPay + extraPay
        val memo = binding.memo.text.toString()

        sendingData = startTime.toString()
        Log.e("시작 시간 확인", startTime.toString())
        Log.e("끝 시간 확인", endTime.toString())
        Log.e("메시지 확인", "wage : $wage extraPay : $extraPay totalPay : $totalPay breakTime: $breakTime holidayPayRate: $holidayPayRate long: $longPayRate nig: $nightPayRate memo: $memo")


        val work = RequestWork(storeId, startTime.toString(), endTime.toString(), wage, 0, 100000, 60, 0, 0, 0, memo)
        retIn.requestWork(authorization = token, body = work).enqueue(object:
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("서버 통신 실패", "${t.message}")
                Toast.makeText(baseContext, "네트워크를 확인해 주세요", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>) {
                if(response.isSuccessful) {
                    Log.e("서버 연결 성공", "${response.code()}")
                    Toast.makeText(baseContext, "기록 추가 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }

                else {
                    Log.e("로그인 실패 4XX", "error : ${response.code()} ${response.errorBody()}")
                    Toast.makeText(baseContext, "기록 추가 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun payCount(startDate:LocalDateTime, endDate:LocalDateTime, holidayPayRate:Int, longPayRate:Int, nightPayRate:Int, wage:Int) {
        /*
        var isHoliday = false
        var isNight = false
        var isOver = false

        if(startTime.dayOfMonth == 1 || startTime.dayOfMonth == 7) {
            isHoliday = true
        }
        val startHour = startTime.hour
        val startMin = startTime.minute
        val endHour = endTime.hour
        val endMin = endTime.minute

        val startNight = LocalTime.of(22, 0, 0)
        val endNight = LocalTime.of(6,0,0)

        if(startHour in 0..6) {
            isNight = true
        }
        else if(startHour in 22..23) {
            isNight = true
        }
        if(abs(startHour - endHour) > 8) {
            isOver = true
        }*/

        val startTime = LocalTime.of(startDate.hour, startDate.minute)
        val endTime = LocalTime.of(endDate.hour, endDate.minute)
        val workDate = LocalDate.of(startDate.year, startDate.monthValue, startDate.dayOfMonth)
        val overtimeDuration = calculateOvertimeDuration(startDate, endDate)
        val overtimeHours = overtimeDuration.toHours().toInt()
        val holidayAllowance = calculateHolidayAllowance(wage.toDouble(), workDate)
    }

    private fun calculateOvertimeDuration(startTime: LocalDateTime, endTime: LocalDateTime): Duration {
        val workDuration = Duration.between(startTime, endTime)
        val normalDuration = Duration.ofHours(8) // 정상 근무 시간: 8시간
        val overtimeDuration = workDuration.minus(normalDuration).abs()
        return overtimeDuration
    }

    fun calculateHolidayAllowance(hourlyRate: Double, workDate: LocalDate): Double {
        val holidayAllowanceRate = 0.5 // 휴일 수당 계수: 0.5배
        var holidayAllowance = 0.0
        if (isWeekend(workDate)) {
            holidayAllowance = hourlyRate * holidayAllowanceRate * 10 // 주간 근무 시간: 10시간
        }
        return holidayAllowance
    }

    fun calculateOvertimeAllowance(hourlyRate: Double, overtimeHours: Int): Double {
        val overtimeAllowanceRate = 1.5 // 연장 수당 계수: 1.5배
        val overtimeAllowance = hourlyRate * overtimeAllowanceRate * overtimeHours
        return overtimeAllowance
    }

    fun calculateTotalPay(hourlyRate: Double, overtimeAllowance: Double, holidayAllowance: Double): Double {
        val totalPay = (hourlyRate * 8) + overtimeAllowance + holidayAllowance
        return totalPay
    }

    fun isWeekend(date: LocalDate): Boolean {
        return date.dayOfWeek.value >= 6
    }
}