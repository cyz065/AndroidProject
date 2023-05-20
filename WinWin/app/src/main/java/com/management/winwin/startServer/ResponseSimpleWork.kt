package com.management.winwin.startServer

import com.management.winwin.Card.Work
import java.time.LocalDateTime

data class ResponseSimpleWork (
    val code:Int,
    val message:String,
    val data:List<Work>
        )