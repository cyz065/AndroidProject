package com.management.winwin.startServer

import com.google.gson.annotations.SerializedName

data class RequestLogin(
    @SerializedName("username")
    val userName:String,
    val password:String
)