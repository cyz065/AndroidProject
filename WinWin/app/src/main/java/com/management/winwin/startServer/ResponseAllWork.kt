package com.management.winwin.startServer

import com.google.gson.annotations.SerializedName

data class ResponseAllWork (
    val code:String,
    val message:String,
    val data:List<WorkDetail>
)