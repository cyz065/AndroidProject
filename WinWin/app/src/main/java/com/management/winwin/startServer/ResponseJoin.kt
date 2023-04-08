package com.management.winwin.startServer

import com.google.gson.annotations.SerializedName

data class ResponseJoin(
    val code:Int,
    val message:String,
    val data:Info
)

data class Info(
    val id:Int,
    @SerializedName("username")
    val userName:String,
    @SerializedName("password")
    val password:String,
    val email:String
)

data class ResponseJoinFail(
    val code:Int,
    val message:String
)