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
    private val url="https://d5ff796c-012c-48be-ac57-8d1ed5524cb1.mock.pstmn.io/"
    val api: ApiHelper

    init {
        var loggingIntercept = HttpLoggingInterceptor()
        loggingIntercept.level=HttpLoggingInterceptor.Level.BODY

        var client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingIntercept)
            .addInterceptor(ResponseInterceptor())
            .addInterceptor(AddCookieInterceptor())
            .build()

        retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(url)
            .build()

        api = retrofit.create(ApiHelper::class.java)
    }
}