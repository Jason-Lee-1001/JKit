package com.jstudio.app.http

import com.jstudio.app.entity.Record
import com.jstudio.app.entity.User
import retrofit2.http.*

interface ApiService {

    @POST("login/doctorLogin")
    @FormUrlEncoded
    @Headers("clientType:MOBILE")
    suspend fun signIn(@Field("content") encryptedContent: String): ResultBody<User>

    @POST("record/myRecordList")
    @FormUrlEncoded
    @Headers("clientType:MOBILE")
    suspend fun recordList(
        @Header("appToken") appToken: String,
        @Header("doctorId") doctorId: Long,
        @Field("content") encryptedContent: String
    ): ResultBody<Record>
}