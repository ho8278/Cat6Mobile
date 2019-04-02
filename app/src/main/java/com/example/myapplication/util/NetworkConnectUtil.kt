package com.example.myapplication.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkConnectUtil{
    companion object {
        fun isConnect(context: Context):Boolean{
            val cm=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo.isConnected
        }
    }
}