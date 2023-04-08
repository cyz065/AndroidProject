package com.management.winwin.Calendar

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class EventDecorator(private val colors:IntArray, dates: Collection<CalendarDay>) : DayViewDecorator {
    private val dates:HashSet<CalendarDay> = HashSet(dates)
    private val color:IntArray = colors

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view!!.addSpan(CustomDotSpan(8F, colors))
    }
}