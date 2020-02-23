package com.jade.jade_mvvm

import com.jade.jade_mvvm.bean.Message
import com.jade.jade_mvvm.helper.retrofit.LiveDataCallAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Service {

    companion object {
        val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.113:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
            .build()

        fun getService() = retrofit.create(Service::class.java)
    }

    @POST("message/getAllMessage")
    fun getAllMessage(): Call<List<Message>>

    @POST("message/getCountMessageFromPosition")
    @FormUrlEncoded
    fun getCountMessageFromPosition(@Field("position") position: Int, @Field("count") count: Int): Call<List<Message>>

    @POST("message/getDetailMessage")
    @FormUrlEncoded
    fun getDetailMessage(@Field("id") id: Int): Call<Message>
}