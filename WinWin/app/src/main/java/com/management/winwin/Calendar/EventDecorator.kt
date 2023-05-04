package com.management.winwin.Calendar

import android.graphics.Color
import android.util.Log
import com.management.winwin.MainActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*
import kotlin.collections.HashSet

class EventDecorator(private val count:Int, dates:Collection<CalendarDay>) : DayViewDecorator {

    private val dates:HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        if(count >= 3) {
            val colors = intArrayOf(Color.RED, Color.BLUE, Color.GREEN)
            view!!.addSpan(CustomDotSpan(8F, colors))
        }
        else if(count == 2) {
            val colors = intArrayOf(Color.BLACK, Color.MAGENTA)
            view!!.addSpan(CustomDotSpan(8F, colors))
        }
        else if(count == 1) {
            val colors = intArrayOf(Color.GRAY)
            view!!.addSpan(CustomDotSpan(8F, colors))
        }
    }
}

/*
class EventDecorator(dates: HashMap<CalendarDay, ArrayList<Int>>, day:CalendarDay) : DayViewDecorator {

    private val dateList = dates
    private val day = day

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dateList.containsKey(day)
        //return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        if(dateList.containsKey(day)) {
            if(dateList[day]!!.size >= 3) {
                Log.e("달력 로그", "3 이상 $day")
                val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
                view!!.addSpan(CustomDotSpan(8F, colors))
            }
            else if(dateList[day]!!.size == 2) {
                Log.e("달력 로그", "2 $day")
                val colors = intArrayOf(Color.BLACK, Color.MAGENTA)
                view!!.addSpan(CustomDotSpan(8F, colors))
            }
            else if(dateList[day]!!.size == 1) {
                Log.e("달력 로그", "1 $day")
                val colors = intArrayOf(Color.BLACK)
                view!!.addSpan(CustomDotSpan(8F, colors))
            }
        }

        /*
        if(dates.size >= 3) {
            val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE)
            view!!.addSpan(CustomDotSpan(8F, colors))
        }
        else if(dates.size == 2) {
            val colors = intArrayOf(Color.BLACK, Color.MAGENTA)
            view!!.addSpan(CustomDotSpan(8F, colors))
        }
        else if(dates.size == 1) {
            val colors = intArrayOf(Color.BLACK)
            view!!.addSpan(CustomDotSpan(8F, colors))
        }*/
    }


}*/