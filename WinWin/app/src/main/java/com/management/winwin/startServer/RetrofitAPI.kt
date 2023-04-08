package com.management.winwin.startServer

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RetrofitAPI {
    @POST("/join")
    @Headers("content-type: application/json")
    fun requestJoin(@Body body : RequestJoin): Call<ResponseJoin>

    @POST("/login")
    fun requestLogin(@Body body: RequestLogin):Call<ResponseLogin>

    @POST("/checkUsername")
    fun requestDuplicate(@Body body: RequestDuplicate):Call<ResponseDuplicate>

    @GET("/api/user/test")
    fun getResponse(
        @Header("Authorization") authorization:String
    ): Call<ResponseBody>
}