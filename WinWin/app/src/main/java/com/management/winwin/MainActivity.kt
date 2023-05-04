package com.management.winwin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.management.winwin.Calendar.*
import com.management.winwin.Card.CardAdapter
import com.management.winwin.Card.Work
import com.management.winwin.databinding.ActivityMainBinding
import com.management.winwin.preference.Application.Companion.prefs
import com.management.winwin.startServer.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityMainBinding
    //val models = ArrayList<Model>()
    //private lateinit var adapter:Adapter

    private lateinit var calendar: MaterialCalendarView
    private val workList = ArrayList<Work>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        requestToServer()

        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        //supportActionBar!!.setIcon(R.drawable.temp_logo)
        supportActionBar!!.setLogo(R.drawable.temp_logo)
        binding.next.setOnClickListener(this)
        binding.nextCalendar.setOnClickListener(this)
        getWorkInfo()
        calendar = binding.calendar

        calendarSetting()

        binding.calendar.setOnMonthChangedListener { _, date ->
            val year = date.year
            val month = date.month
            binding.calendarTitle.text = makeTitle(year, month)
       }
    }

    private fun makeTitle(year:Int, month:Int) :String{
        Log.e("TITLE", "$year $month")
        val calendarHeaderBuilder = StringBuilder()
        calendarHeaderBuilder.append(year)
            .append("년")
            .append(" ")
            .append(month + 1)
            .append("월")
        return calendarHeaderBuilder.toString()
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

        val adapter = CardAdapter(this, workList)
        binding.recyclerView.adapter = adapter


        // 카드 뷰에서 목록 아이템 선택시 달력 화면으로 넘어간다.
        adapter.itemClick = object:CardAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val intent = Intent(baseContext, CalendarActivity::class.java)
                intent.putExtra("StoreName", workList[position].workSite)
                intent.putExtra("StoreCode", workList[position].code)
                startActivity(intent)
            }
        }
    }


    private fun calendarSetting() {
        val startTimeCalendar = Calendar.getInstance()
        var endTimeCalendar = Calendar.getInstance()

        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDate = startTimeCalendar.get(Calendar.DATE)

        Log.e("달력", "$currentYear $currentMonth $currentDate")
        // 주말 커스텀
        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()

        // 오늘 날짜 커스텀
        val todayDecorator = TodayDecorator(this)

        binding.calendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(currentYear - 1, currentMonth, 1))
            //.setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()

        // 월, 일 한글 설정
        binding.calendar.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        binding.calendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        // 토, 일요일 파란색, 빨간색 설정
        binding.calendar.addDecorators(sundayDecorator, saturdayDecorator, todayDecorator)

        // 달력 기본 폰트 설정
        binding.calendar.setDateTextAppearance(R.style.CustomDateTextAppearance)
        binding.calendar.setWeekDayTextAppearance(R.style.CustomWeekDayAppearance)
        binding.calendar.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)

        binding.calendar.topbarVisible = false
        binding.calendarTitle.text = makeTitle(currentYear, currentMonth).toString()

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
        /*
        val eventDates = ArrayList<CalendarDay>()

        val dayInstance = Calendar.getInstance()
        dayInstance.set(currentYear, currentMonth, currentDate)
        var day = CalendarDay.from(dayInstance)
        eventDates.add(day)*/
        //binding.calendar.addDecorator(TextDecorator(eventDates, "270000"))
        //binding.calendar.addDecorator(EventDecorator(eventDates, "27000"))


        // 점 찍기
        // CalendarDay객체를 ArrayList에 추가
        // threeColors배열에 색상 추가
        // 추가한 색상 개수 만큼 list에 존재하는 날짜에 점 찍기

        val eventDates = ArrayList<CalendarDay>()
        var dot_day = currentDate

        var date = Calendar.getInstance()
        date.set(currentYear, currentMonth, dot_day)
        var day = CalendarDay.from(date)
        eventDates.add(day)

        dot_day = currentDate - 3
        date = Calendar.getInstance()
        date.set(currentYear, currentMonth, dot_day)
        day = CalendarDay.from(date)
        eventDates.add(day)

        dot_day = currentDate - 5
        date = Calendar.getInstance()
        date.set(currentYear, currentMonth, dot_day)
        day = CalendarDay.from(date)

        val tmp = ArrayList<CalendarDay>()
        tmp.add(day)

        val threeColors = IntArray(3)
        threeColors[0] = Color.BLUE
        threeColors[1] = Color.RED
        threeColors[2] = Color.GREEN

        val twoColors = IntArray(2)
        twoColors[0] = Color.BLACK
        twoColors[0] = Color.MAGENTA

        binding.calendar.addDecorator(EventDecorator(3, eventDates))
        binding.calendar.addDecorator(EventDecorator(2, tmp))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.notification -> {
                Toast.makeText(this, "알림 클릭", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestToServer() {
        val retIn = RetrofitService.getRetrofitInstance().create(RetrofitAPI::class.java)
        val token = prefs.getString("token", "")
        Log.e("통신 토큰", token)
        if(token == "") {
            Log.e("통신 토큰 에러", token)
            return
        }

        retIn.getResponse(token).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val html:String = response.body()!!.string()
                    Log.e("성공 메인", html)
                    Toast.makeText(this@MainActivity, html, Toast.LENGTH_SHORT).show()

                }
                catch (e : Exception) {
                    Log.e("에러", "${e.printStackTrace()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("완전 실패", "${t.message}")
            }
        })
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.next-> {
                Toast.makeText(this, "업장 조회 클릭", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, WorkInfoActivity::class.java)
                startActivity(intent)
            }
            R.id.nextCalendar -> {
                Toast.makeText(this, "달력 조회 클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

