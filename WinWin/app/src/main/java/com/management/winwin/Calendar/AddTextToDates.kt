package com.management.winwin.Calendar

import android.graphics.Canvas
import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.LineBackgroundSpan
import android.text.style.RelativeSizeSpan

class AddTextToDates(text:String) : LineBackgroundSpan {
    private var total = text
    private var test = SpannableString(total)


    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lnum: Int
    ) {
        canvas.drawText(total,((left+right)/20).toFloat(),(bottom+30).toFloat(),paint)
    }

}