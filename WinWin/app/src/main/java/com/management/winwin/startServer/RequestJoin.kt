package com.management.winwin.startServer

import com.google.gson.annotations.SerializedName

data class RequestJoin(
    @SerializedName("username")
    var userName:String,
    @SerializedName("password")
    var passWord:String,

    var email:String
)