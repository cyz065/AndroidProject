package com.management.winwin.Calendar

data class DetailInfo(val date:String, val workTime:String,
                      val money:String, val rest:String, val total:String, var isExpand:Boolean = false)