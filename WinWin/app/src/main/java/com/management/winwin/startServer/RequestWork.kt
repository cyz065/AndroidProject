package com.management.winwin.startServer

import com.google.gson.annotations.SerializedName

data class RequestWork (
    val storeId:Int,
    val startTime:String,
    val endTime:String,
    val wage:Int,
    val extraPay:Int,
    val totalPay:Int,
    @SerializedName("breaktime")
    val breakTime:Int,
    val holidayPayRate:Int,
    val longPayRate:Int,
    val nightPayRate:Int,
    val memo:String)