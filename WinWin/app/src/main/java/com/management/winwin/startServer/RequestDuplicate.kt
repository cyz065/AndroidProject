package com.management.winwin.startServer

import com.google.gson.annotations.SerializedName

data class RequestDuplicate(
    @SerializedName("username")
    val userName:String
)