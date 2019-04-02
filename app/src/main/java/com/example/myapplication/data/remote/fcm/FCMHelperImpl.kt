package com.example.myapplication.data.remote.fcm

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object FCMHelperImpl{
    private val url="https://fcm.googleapis.com/fcm/"
    val api:FCMHelper

    init{
        var loggingIntercept = HttpLoggingInterceptor()

        var client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingIntercept)
            .build()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(url)
            .build()

        api=retrofit.create(FCMHelper::class.java)
    }

}