package com.example.myapplication.data.remote.api

import android.content.Context
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import okhttp3.Interceptor
import okhttp3.Response

class AddCookieInterceptor(val context:Context):Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder=chain.request().newBuilder()

        val stringSet= DataManager.getInstance(context.applicationContext).getItem<MutableSet<String>>(PreferenceHelperImpl.COOKIES)

        stringSet.forEach {
            builder.addHeader("Cookie",it)
        }

        builder.removeHeader("User-Agent")
            .addHeader("User-Agent","Android")

        return chain.proceed(builder.build())
    }
}