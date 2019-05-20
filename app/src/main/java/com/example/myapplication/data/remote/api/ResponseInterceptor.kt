package com.example.myapplication.data.remote.api

import android.content.Context
import android.util.Log
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.view.main.AppInitialize
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.headers("Set-Cookie").isEmpty()) {
            val cookies = mutableSetOf<String>()

            response.headers("Set-Cookie")
                .forEach {
                    val last = it.indexOfFirst { char -> char == ';' }
                    val subString = it.substring(0,last)
                    Log.e("TEST",subString)
                    cookies.add(subString)
                }

            AppInitialize.dataSource.saveItem(PreferenceHelperImpl.COOKIES, cookies)
        }

        return response

    }
}