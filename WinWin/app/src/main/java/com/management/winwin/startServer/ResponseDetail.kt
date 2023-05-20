package com.management.winwin.startServer

import com.management.winwin.Calendar.DetailInfo

data class ResponseDetail (
    val code:String,
    val message:String,
    val data:List<DetailInfo>)
