package com.management.winwin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.management.winwin.Calendar.*
import com.management.winwin.Card.CardAdapter
import com.management.winwin.Card.Work
import com.management.winwin.databinding.ActivityCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class CalendarActivity : AppCompatActivity(), View.OnClickListener, OnMonthChangedListener, OnDateSelectedListener {
    private lateinit var binding:ActivityCalendarBinding
    private var storeCode:String? = null
    private var storeName:String? = null
    private val infoList = ArrayList<DetailInfo>()
    private lateinit var adapter:DetailInfoAdapter

    // 각 날짜 별로 이벤트 데이가 추가
    // ex) 04-26 : 일정이 3개
    private val schedule = HashMap<CalendarDay, Int>()
    private val threeEvents = ArrayList<CalendarDay>()
    private val twoEvents = ArrayList<CalendarDay>()
    private val oneEvent = ArrayList<CalendarDay>()

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

        calendarSetting()
        binding.next.setOnClickListener(this)
        binding.before.setOnClickListener(this)
        binding.monthCalendar.setOnMonthChangedListener(this)
        binding.monthCalendar.setOnDateChangedListener(this)
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

    private fun calendarSetting() {
        if(storeName == null && storeCode == null) {
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
        Log.e("달력 로그", "${schedule}")
        //binding.monthCalendar.addDecorator(EventDecorator(schedule))


        adapter = DetailInfoAdapter(this, infoList)
        val today = Calendar.getInstance()

        //binding.information.setHasFixedSize(true)
        today.set(currentYear, currentMonth, currentDate)
        makeInfoList(CalendarDay.from(today))
        binding.information.setHasFixedSize(true)
        binding.information.adapter = adapter
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.next-> {
                binding.monthCalendar.goToNext()
            }

            R.id.before-> {
                binding.monthCalendar.goToPrevious()
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
        binding.date.text = String.format("%02d일 %s요일", day, dayOfWeek[dayArray[0]])


        // 일정 변경 시 아래 리스트 목록 변경
        val size = schedule.getOrDefault(date, 0)
        infoList.clear()

        for(i in 0 until size) {
            infoList.add(DetailInfo(date.toString(), "test", "5000", "test", "100000"))
        }
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
                infoList.add(DetailInfo(day.toString(), "17:00", "100000", "30분", "400000"))
            }
        }
    }

}