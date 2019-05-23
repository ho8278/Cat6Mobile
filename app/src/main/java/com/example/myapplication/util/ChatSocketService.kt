package com.example.myapplication.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.model.ChatInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.*
import java.util.concurrent.TimeUnit

class ChatSocketService: Service(){
    val TAG =ChatSocketService::class.java.simpleName
    val url = "https://cb225b54.ngrok.io"
    companion object {
        var serviceIntent :Intent? = null
        var socket:Socket? =null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceIntent = intent
        Log.e(TAG,"Start Service")
        try{
            socket = IO.socket(url)
            socket?.connect()
            socket?.on(Socket.EVENT_CONNECT){
                Log.e(TAG,it.toString())
            }
            socket?.on("TEST"){
                Toast.makeText(applicationContext,"뭔가 왔다!",Toast.LENGTH_SHORT).show()
                Log.e(TAG,it.toString())
            }
        }catch (e:URISyntaxException){
            e.printStackTrace()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceIntent=null
        setAlarm()
        socket?.disconnect()
    }

    private fun setAlarm(){
        val calendar = Calendar.getInstance()
        calendar.timeInMillis=System.currentTimeMillis()
        calendar.add(Calendar.SECOND,1)
        val intent = Intent(this,AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,1,intent,0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}