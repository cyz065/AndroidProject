package com.management.winwin.Calendar

import android.content.Context
import androidx.core.content.ContextCompat
import com.management.winwin.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator(context: Context) :DayViewDecorator {
    private var date = CalendarDay.today()
    private val drawable = ContextCompat.getDrawable(context, R.drawable.today)

    // true 리턴 시 설정한 드로어블 적용 가능
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(drawable!!)
    }
}