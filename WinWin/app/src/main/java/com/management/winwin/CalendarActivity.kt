package com.management.winwin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.management.winwin.Calendar.*
import com.management.winwin.databinding.ActivityCalendarBinding
import com.management.winwin.preference.Application.Companion.prefs
import com.management.winwin.startServer.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CalendarActivity : AppCompatActivity(), View.OnClickListener, OnMonthChangedListener, OnDateSelectedListener {
    private lateinit var binding:ActivityCalendarBinding
    private var storeCode:String? = null
    private var storeName:String? = null
    private var storeId = -1

    private lateinit var adapter:DetailInfoAdapter
    private val retIn = RetrofitService.getRetrofitInstance().create(RetrofitAPI::class.java)
    private val token = prefs.getString("token", "")

    // 각 날짜 별로 이벤트 데이가 추가
    // ex) 04-26 : 일정이 3개
    // 각 날짜 별 존재 일정 리스트
    private var infoList = ArrayList<DetailInfo>()
    // 일자별 모든 일정기록
    private val allInfo = HashMap<CalendarDay, ArrayList<DetailInfo>>()
    // 일자별 일정의 개수
    private val schedule = HashMap<CalendarDay, Int>()

    private val dayOfWeek = mutableMapOf<String, String>(
        ("Mon" to "월"), ("Sun" to "일"), ("Tue" to "화"),
        ("Wed" to "수"), ("Thu" to "목"), ("Fri" to "금"), ("Sat" to "토")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setActionBar()

        storeCode = intent.getStringExtra("StoreCode")
        storeName = intent.getStringExtra("StoreName")
        storeId = intent.getIntExtra("StoreId", -1)

        Log.e("캘린더 인텐트", "$storeCode, $storeName, $storeId")
        val startSearchTime = intent.getStringExtra("Start")
        val endSearchTime = intent.getStringExtra("End")

        if (startSearchTime != null && endSearchTime != null)
            calendarSetting(startSearchTime, endSearchTime)

        binding.next.setOnClickListener(this)
        binding.before.setOnClickListener(this)
        binding.monthCalendar.setOnMonthChangedListener(this)
        binding.monthCalendar.setOnDateChangedListener(this)
        binding.addButton.setOnClickListener(this)
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

    private fun numToKor(day:Int):String {
        return when(day) {
            Calendar.SUNDAY -> "일요일"
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY-> "화요일"
            Calendar.WEDNESDAY-> "수요일"
            Calendar.THURSDAY-> "목요일"
            Calendar.FRIDAY-> "금요일"
            Calendar.SATURDAY-> "토요일"
            else-> ""
        }
    }

    private fun calendarSetting(startSearchTime:String, endSearchTime:String) {
        if(storeName == null && storeCode == null && storeId < 0) {
            finish()
            return
        }

        val startTimeCalendar = Calendar.getInstance()
        var endTimeCalendar = Calendar.getInstance()

        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDate = startTimeCalendar.get(Calendar.DATE)
        val currentDayOfWeek = startTimeCalendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeek = numToKor(currentDayOfWeek)

        binding.storeName.text = storeName
        binding.month.text = String.format("%02d월", currentMonth + 1)
        binding.year.text = String.format("%d년", currentYear)
        binding.date.text = String.format("%02d일 %s", currentDate, dayOfWeek)

        // 주말 커스텀
        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()

        // 오늘 날짜 커스텀
        val todayDecorator = TodayDecorator(this)

        binding.monthCalendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(currentYear - 1, currentMonth, 1))
            .commit()

        // 월, 일 한글 설정
        binding.monthCalendar.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        binding.monthCalendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        // 토, 일요일 파란색, 빨간색 설정
        binding.monthCalendar.addDecorators(sundayDecorator, saturdayDecorator, todayDecorator)

        // 달력 기본 폰트 설정
        binding.monthCalendar.setDateTextAppearance(R.style.CustomDateTextAppearance)
        binding.monthCalendar.setWeekDayTextAppearance(R.style.CustomWeekDayAppearance)
        binding.monthCalendar.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)

        binding.monthCalendar.topbarVisible = false
        //binding.monthCalendar.text = makeTitle(currentYear, currentMonth).toString()

/*
        // 날짜 선택 이벤트 디자인
        binding.calendar.setOnRangeSelectedListener(object: OnRangeSelectedListener {
            override fun onRangeSelected(
                widget: MaterialCalendarView,
                dates: MutableList<CalendarDay>
            ) {
                TODO("Not yet implemented")
            }

        })*/

        // 가운데 년 / 월 커스텀
/*
        binding.calendar.setTitleFormatter { day ->
            val calendarHeaderElements = mutableListOf<String>()
            calendarHeaderElements.add(day.year.toString())
            calendarHeaderElements.add((day.month + 1).toString())

            val calendarHeaderBuilder = StringBuilder()
            calendarHeaderBuilder.append(calendarHeaderElements[0])
                .append("년")
                .append(" ")
                .append(calendarHeaderElements[1])
                .append("월")
            calendarHeaderBuilder.toString()
        }
*/

        // 점 찍기
        // CalendarDay객체를 ArrayList에 추가
        // threeColors배열에 색상 추가
        // 추가한 색상 개수 만큼 list에 존재하는 날짜에 점 찍기
        /*
        workList.add(Work("GS25 강남점", "184567", "03.01 ~ 03.31", "270000"))
        workList.add(Work("CU 강남점", "172459", "02.01 ~ 02.28", "300000"))
        workList.add(Work("스타벅스 강남점", "987654", "02.18 ~ 02.25", "100000"))
        workList.add(Work("메가커피 강남점", "487234", "01.18 ~ 01.31", "200000"))
        workList.add(Work("이디야 커피 강남점", "165832", "03.01 ~ 04.19", "450000"))
        workList.add(Work("CGV 강남점", "125345", "04.01 ~ 04.03", "25000"))
        workList.add(Work("메가박스 강남점", "029863", "05.01 ~ 05.05", "10000"))
        workList.add(Work("롯데리아 강남점", "472943", "04.19 ~ 04.26", "23000"))

        val adapter = CardAdapter(this, workList)
        binding.recyclerView.adapter = adapter*/
/*
        var dot_day = currentDate

        var date = Calendar.getInstance()
        date.set(currentYear, currentMonth, dot_day)
        var day = CalendarDay.from(date)

        insert(day)
        insert(day)
        insert(day)

        threeEvents.add(day)
        val decorators = mutableListOf<EventDecorator>()

        val size = schedule[day]!!
        decorators.add(EventDecorator(size, threeEvents))




        //eventDates.add(day)
        //eventDates.add(day)
        //eventDates.add(day)
        //infoList.add(DetailInfo(day.toString(), "12:00", "10000", "30", "40000"))

        //binding.monthCalendar.addDecorator(EventDecorator(eventDates))
        //val adapter = DetailInfoAdapter(this, infoList)
        //binding.infomation.adapter = adapter

        dot_day = currentDate - 7
        date = Calendar.getInstance()
        date.set(currentYear, currentMonth, dot_day)
        day = CalendarDay.from(date)

        insert(day)
        insert(day)
        twoEvents.add(day)
        decorators.add(EventDecorator(schedule[day]!!, twoEvents))
        //schedule[day]!!.add(day)
        //schedule[day]!!.add(day)
        //eventDates = ArrayList()
        //eventDates.add(day)
        //eventDates.add(day)
        //binding.monthCalendar.addDecorator(EventDecorator(eventDates))

        dot_day = currentDate - 5
        date = Calendar.getInstance()
        date.set(currentYear, currentMonth, dot_day)
        day = CalendarDay.from(date)
        insert(day)
        oneEvent.add(day)
        decorators.add(EventDecorator(schedule[day]!!, oneEvent))
        //schedule[day]!!.add(day)
        //eventDates = ArrayList()
        //eventDates.add(day)
        binding.monthCalendar.addDecorators(decorators)
        //binding.monthCalendar.addDecorator(EventDecorator(schedule, day))
        Log.e("달력 로그", "$schedule")
        //binding.monthCalendar.addDecorator(EventDecorator(schedule))*/

        val oneEvent = ArrayList<CalendarDay>()
        val twoEvent = ArrayList<CalendarDay>()
        val threeEvent = ArrayList<CalendarDay>()

        val schedule = HashMap<CalendarDay, Int> ()
        Log.e("근무지별 날짜 현황", "$startSearchTime $endSearchTime $storeId")

        retIn.getStoreWork(authorization = token, startSearchTime = startSearchTime, endSearchTime = endSearchTime, storeId = storeId)
            .enqueue(object:Callback<ResponseDetail> {
                override fun onResponse(call: Call<ResponseDetail>, response: Response<ResponseDetail>) {
                    if(response.isSuccessful) {
                        val body = response.body()
                        val list = body!!.data
                        val calendar = Calendar.getInstance()
                        Log.e("근무지별 달력 list", "${list.size}")
                        for(item in list) {
                            val start = item.startTime
                            val end = item.endTime
                            val startTime = LocalDateTime.parse(start)
                            val endTime = LocalDateTime.parse(end)
                            item.workTime = timeCalc(startTime, endTime)

                            calendar.set(startTime.year, startTime.monthValue - 1, startTime.dayOfMonth)
                            val day = CalendarDay.from(calendar)
                            val date = dateFormatter(day, true)
                            item.date = date

                            if(allInfo[day] == null) {
                                allInfo[day] = ArrayList()
                                allInfo[day]!!.add(item)
                            } else {
                                allInfo[day]!!.add(item)
                            }
                            schedule[day] = schedule.getOrDefault(day, 0) + 1
                            Log.e("근무지별 날짜", day.toString())
                        }

                        Log.e("근무지별 달력 현황", schedule.toString())
                        Log.e("근무지별 달력 현황", allInfo.toString())
                        for((day, _) in schedule) {
                            when(schedule[day]) {
                                1 -> oneEvent.add(day)
                                2 -> twoEvent.add(day)
                                else -> threeEvent.add(day)
                            }
                        }

                        val decorators = mutableListOf<EventDecorator>()
                        decorators.add(EventDecorator(3, threeEvent))
                        decorators.add(EventDecorator(2, twoEvent))
                        decorators.add(EventDecorator(1, oneEvent))
                        binding.monthCalendar.addDecorators(decorators)

                        //val today = Calendar.getInstance()

                        //binding.information.setHasFixedSize(true)
                        //today.set(currentYear, currentMonth, currentDate)
                        //makeInfoList(CalendarDay.from(today))
                        //binding.information.setHasFixedSize(true)
                        //binding.information.adapter = adapter
                        //Log.e("달력 확인", "$oneEvent $twoEvents $threeEvents")

                        //adapter = DetailInfoAdapter(baseContext, infoList)

                        val today = Calendar.getInstance()
                        today.set(currentYear, currentMonth, currentDate)
                        infoList = allInfo.getOrDefault(CalendarDay.from(today), ArrayList())
                        adapter = DetailInfoAdapter(baseContext, infoList)
                        binding.information.setHasFixedSize(true)
                        binding.information.adapter = adapter
                    }

                    else {
                        Log.e("서버 연결 실패", "실패 ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                    Log.e("서버 연결 완전 실패", "완전 실패 ${t.message}")
                }
            })

        // 밑에 부분
        //adapter = DetailInfoAdapter(this, infoList)
        //val today = Calendar.getInstance()

        //binding.information.setHasFixedSize(true)
        //today.set(currentYear, currentMonth, currentDate)
        //makeInfoList(CalendarDay.from(today))
        //binding.information.setHasFixedSize(true)
        //binding.information.adapter = adapter
        //Log.e("달력 확인", "$oneEvent $twoEvents $threeEvents")
    }

    private fun dateFormatter(date:CalendarDay, dot:Boolean):String {
        val sb = StringBuilder()
        val year = String.format("%d.",date.year)
        val month = String.format("%02d.", date.month + 1)
        val tmp = date.date.toString().split(" ")
        val day = String.format("%02d", date.day)
        val week = String.format("%s", dayOfWeek[tmp[0]])

        sb.append(year).append(month).append(day).append(" (").append(week).append(")")
        return sb.toString()
    }

    private fun dateFormatter(date:CalendarDay):String{
        val sb = StringBuilder()
        val year = String.format("%d년", date.year)
        val month = String.format("%02d월", date.month + 1)
        val tmp = date.date.toString().split(" ")
        val day = String.format("%02d일", date.day)
        val week = String.format("%s요일", dayOfWeek[tmp[0]])
        sb.append(year).append(month).append(day).append(week)
        return sb.toString()
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.next-> {
                binding.monthCalendar.goToNext()
            }

            R.id.before-> {
                binding.monthCalendar.goToPrevious()
            }
            R.id.addButton-> {
                val intent = Intent(this, AddActivity::class.java)
                intent.putExtra("storeId", storeId)
                intent.putExtra("storeName", storeName)
                Log.e("달력에서 추가버튼", "$storeId $storeName")
                startActivity(intent)
            }
        }
    }

    override fun onMonthChanged(widget: MaterialCalendarView, date: CalendarDay) {
        // 달이 변경 되면 서버 요청을 통해 data 수신 & decorator 수행
        binding.month.text = String.format("%02d월", date.month + 1)
        binding.year.text = String.format("%d년", date.year)
        Log.e("달력", "$date")
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        val day = date.day
        val dayArray = date.date.toString().split(" ")

        Log.e("달력 요일", "$day ${dayOfWeek[dayArray[0]]}")
        Log.e("변경 전 infoList", "$infoList ${allInfo[date]}")
        binding.date.text = String.format("%02d일 %s요일", day, dayOfWeek[dayArray[0]])
        infoList.clear()
        val size = schedule.getOrDefault(date, 0)
        val tmp = allInfo.getOrDefault(date, ArrayList())

        for(item in tmp) {
            infoList.add(item)
        }

        //infoList = allInfo.getOrDefault(date, ArrayList())
        // 일정 변경 시 아래 리스트 목록 변경
        //val size = schedule.getOrDefault(date, 0)
        //val tmp = allInfo.getOrDefault(date, ArrayList())

        //for(i in 0 until size) {
        //    infoList.add(tmp[i])
        //}

        //for(i in 0 until size) {
            //val dateFormat = dateFormatter(date, true)
            //infoList.add(DetailInfo(dateFormat, "test", "5000", "test", "100000"))
        //}]
        Log.e("변경 후 infoList", "$infoList ${allInfo[date]}")
        adapter.notifyDataSetChanged()
    }

    private fun insert(day:CalendarDay) {
        schedule[day] = schedule.getOrDefault(day, 0) + 1
    }


    // 초기 날짜에 대하여 list만들기
    private fun makeInfoList(day:CalendarDay) {
        if(schedule.containsKey(day)) {
            val count = schedule[day]!!
            for(i in 0 until count) {
                Log.e("존재", "존재")
                val dayFormatter = dateFormatter(day, true)
                //infoList.add(DetailInfo(dayFormatter, "17:00", "100000", "30분", "400000"))
            }
        }
    }

    private fun timeCalc(start:LocalDateTime, end:LocalDateTime):String {
        val duration = Duration.between(start, end)
        val minutes = duration.toMinutes()
        val sb = StringBuilder()
        val hours = (minutes / 60).toInt()
        val min = (minutes % 60).toInt()
        val minFormat = String.format("%02d분", min)

        if(hours > 0) sb.append(hours).append("시간").append(minFormat)
        else sb.append(minFormat)

        return sb.toString()
    }

    override fun onResume() {
        val day = Calendar.getInstance()
        val searchDate = intent.getStringExtra("date")
        Log.e("searchDate", "$searchDate")

        super.onResume()
    }

}