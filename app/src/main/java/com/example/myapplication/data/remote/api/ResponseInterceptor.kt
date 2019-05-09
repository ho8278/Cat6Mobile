package com.example.myapplication.data.remote.api

import android.content.Context
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.view.main.AppInitialize
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor:Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val response=chain.proceed(chain.request())

        if(!response.headers("Set-Cookie").isEmpty()){
            val cookies=HashSet<String>()

            response.headers("Set-Cookie")
                .forEach { cookies.add(it) }

            AppInitialize.dataSource.saveItem(PreferenceHelperImpl.COOKIES,cookies)
        }

        return response

    }
}