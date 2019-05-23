package com.example.myapplication.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val intent = Intent(context,RestartService::class.java)
            context?.startForegroundService(intent)
        }else{
            val intent = Intent(context, ChatSocketService::class.java)
            context?.startService(intent)
        }
    }
}