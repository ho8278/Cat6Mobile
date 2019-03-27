package com.example.myapplication.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiHelperImpl {
    private val retrofit: Retrofit
    val api: ApiHelper

    init {
        var loggingIntercept = HttpLoggingInterceptor()

        var client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingIntercept)
            .build()

        retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(ApiHelper::class.java)
    }

}