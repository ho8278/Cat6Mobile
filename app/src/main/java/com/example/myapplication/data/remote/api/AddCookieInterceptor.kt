package com.example.myapplication.data.remote.api

import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.view.main.AppInitialize
import okhttp3.Interceptor
import okhttp3.Response

class AddCookieInterceptor:Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder=chain.request().newBuilder()

        val stringSet= AppInitialize.dataSource.getItem<MutableSet<String>>(PreferenceHelperImpl.COOKIES)

        stringSet.forEach {
            builder.addHeader("Cookie",it)
        }

        builder.removeHeader("User-Agent")
            .addHeader("User-Agent","Android")

        return chain.proceed(builder.build())
    }
}