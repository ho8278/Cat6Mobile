package com.example.myapplication.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.view.main.MainActivity

class RestartService:Service(){
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        return START_NOT_STICKY
    }

    fun createNotification() {
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
            .setContentTitle("")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(2,noti)

        val chatIntent = Intent(this, ChatSocketService::class.java)
        startService(chatIntent)
        stopForeground(true)
        stopSelf()

    }
}