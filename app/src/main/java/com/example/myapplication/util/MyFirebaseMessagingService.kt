package com.example.myapplication.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.User
import com.example.myapplication.view.main.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = MyFirebaseMessagingService::class.java.simpleName

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Token: " + token)
    }

    fun sendTokenToServer(token: String) {
        //TODO:서버에 토큰값과 유저 정보 저장 및 ROOM에 저장
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val simpleDate = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.KOREA)
        val chatinfoID =
            DataManager.getInstance(applicationContext).getItem<String>(PreferenceHelperImpl.RECENT_CHATINFO_ID)

        val remoteInfoID = remoteMessage?.data?.get("id") ?: "null"
        if (chatinfoID == remoteInfoID)
            return

        if(remoteMessage?.from=="main"){
            broadCastMessage(remoteMessage.data)
            return
        }

        val info = remoteMessage?.data.run {
            ChatInfo(
                UUID.randomUUID().toString()
                , this?.get("sendId") ?: ""
                , this?.get("roomId") ?: ""
                , simpleDate.parse(this?.get("sendDate"))
                , this?.get("message") ?: ""
            )
        }
        createNotification(info)
        DataManager.getInstance(applicationContext).receiveMessage(info)
        //TODO("ROOM에 채팅정보 저장")
    }

    fun broadCastMessage(data:MutableMap<String,String>){
        if(data.get("id")!=null){
            data.get("id").apply {
                FirebaseMessaging.getInstance().subscribeToTopic(this)
            }
            return
        }
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
            .setContentTitle(chatInfo.sendUserId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText(chatInfo.message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notiManager.notify(11, noti)

    }
}