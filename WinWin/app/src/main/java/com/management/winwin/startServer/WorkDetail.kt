package com.management.winwin.startServer

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class WorkDetail (
    val id:Int,
    val userId:Int,
    @SerializedName("username")
    val userName:String,
    val storeId:Int,
    val storeName:String,
    val createDate:String,
    val startTime:String,
    val endTime:String,
    val wage:Int,
    val extraPay:Int,
    val totalPay:Int,
    val breakTime:Int,
    val holidayPayRate:Int,
    val longPayRate:Int,
    val nightPayRate:Int,
    val workState:String,
    val memo:String,
    val startWorkId:Int)