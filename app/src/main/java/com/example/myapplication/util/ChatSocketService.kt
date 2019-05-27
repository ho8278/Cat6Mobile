package com.example.myapplication.util

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.view.main.AppInitialize
import com.example.myapplication.view.main.MainActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.*

class ChatSocketService: Service(){
    val TAG =ChatSocketService::class.java.simpleName
    val url = "http://202.31.202.161:80"
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

            socket?.on("receive"){
                val jsonParser = JsonParser()
                val json = jsonParser.parse(it[0] as String)
                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                val fromJson = gson.fromJson<ChatInfo>(json,ChatInfo::class.java)
                Log.e(TAG,"${fromJson.chatinfo_id}  ${fromJson.send_user_id}  ${fromJson.chatroom_id}  ${fromJson.send_date}  ${fromJson.message}")

                AppInitialize.dataSource.receiveMessage(fromJson)
                if(fromJson.chatinfo_id != AppInitialize.dataSource.getItem(PreferenceHelperImpl.RECENT_CHATINFO_ID))
                    createNotification(fromJson)
            }
        }catch (e:URISyntaxException){
            e.printStackTrace()
        }
        return START_NOT_STICKY
    }


    fun createNotification(chatInfo: ChatInfo) {
        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DataManager.getInstance(applicationContext).let {
                var channelId = it.getItem<String>(PreferenceHelperImpl.CHANNEL_ID)
                var channelName = it.getItem<String>(PreferenceHelperImpl.CHANNEL_NAME)
                if (channelId == "" || channelName == "") {
                    it.saveItem(PreferenceHelperImpl.CHANNEL_ID, "channel1")
                    it.saveItem(PreferenceHelperImpl.CHANNEL_NAME, "channel1")
                    channelId = "channel1"
                    channelName = "channel1"

                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).run {
                        description = "Chat Channel"
                        enableLights(true)
                        lightColor = Color.BLUE
                        enableVibration(true)
                        vibrationPattern = longArrayOf(100, 200, 100, 200)
                        notiManager.createNotificationChannel(this)
                    }
                }
            }
        }
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val noti = NotificationCompat.Builder(applicationContext, "channel1")
            .setContentTitle(chatInfo.send_user_id)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(chatInfo.message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notiManager.notify(11, noti)

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
        calendar.add(Calendar.MILLISECOND,300)
        val intent = Intent(this,AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,1,intent,0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}