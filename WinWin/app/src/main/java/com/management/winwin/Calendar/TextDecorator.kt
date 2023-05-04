package com.management.winwin.Calendar

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.style.LineBackgroundSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TextDecorator (dates: List<CalendarDay>, money:String) : DayViewDecorator {
    private val dates: HashSet<CalendarDay> = HashSet(dates)
    var totalMoney = money

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(AddTextToDates(totalMoney))
        view.addSpan(RelativeSizeSpan(1.0f))
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.setDaysDisabled(true)
    }

}
