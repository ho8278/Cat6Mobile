package com.example.myapplication.data.remote.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelperImpl {
    private val retrofit: Retrofit
    //http://180.71.228.163:8080
    private val url="http://180.71.228.163:8080/"
    val api: ApiHelper

    init {
        var loggingIntercept = HttpLoggingInterceptor()
        loggingIntercept.level=HttpLoggingInterceptor.Level.BODY

        var client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingIntercept)
            .addInterceptor(AddCookieInterceptor())
            .addInterceptor(ResponseInterceptor())
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .baseUrl(url)
            .build()

        api = retrofit.create(ApiHelper::class.java)
    }
}