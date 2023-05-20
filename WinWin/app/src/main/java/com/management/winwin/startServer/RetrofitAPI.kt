package com.management.winwin.startServer

import com.management.winwin.Card.Work
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.time.LocalDateTime

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

    @GET("/test/api/worker/work")
    fun getAllWork(
        @Header("Authorization") authorization: String,
        @Query("startSearchTime") startSearchTime:String,
        @Query("endSearchTime") endSearchTime:String,
    ): Call<ResponseAllWork>

    @GET("/test/api/worker/simpleWork")
    fun getSimpleWork(
        @Header("Authorization") authorization:String,
        @Query("startSearchTime") startSearchTime: String,
        @Query("endSearchTime") endSearchTime:String
    ): Call<ResponseSimpleWork>

    @GET("/test/api/worker/work")
    fun getStoreWork(
        @Header("Authorization") authorization: String,
        @Query("startSearchTime") startSearchTime: String,
        @Query("endSearchTime") endSearchTime: String,
        @Query("storeId") storeId:Int
    ): Call<ResponseDetail>


    @POST("/test/api/worker/work")
    fun requestWork(
        @Header("Authorization") authorization: String,
        @Body body:RequestWork
    ):Call<ResponseBody>
}