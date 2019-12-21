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
import com.example.myapplication.view.main.AppInitialize
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
        val currentUserID = AppInitialize.dataSource.getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
        /*
        val chatinfoID =
            AppInitialize.dataSource.getItem<String>(PreferenceHelperImpl.RECENT_CHATINFO_ID)
        val remoteInfoID = remoteMessage?.data?.get("chatinfo_id") ?: "null"
        if (chatinfoID == remoteInfoID)
            return
        */
        val sendID = remoteMessage?.data?.get("send_user_id") ?: ""
        if(sendID == currentUserID)
            return

        if (remoteMessage?.from == "/topics/main") {
            broadCastMessage(remoteMessage.data)
            return
        }else{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            val chatInfo = remoteMessage?.data?.run {
                val date = get("send_date") ?: ""
                ChatInfo(get("chatinfo_id") ?: "",
                    get("send_user_id") ?: "",
                    get("chatroom_id") ?: "",
                    dateFormat.parse(date),
                    get("message") ?: "")
            }
            createNotification(chatInfo!!)
        }
    }
    
    fun createNotification(chatInfo: ChatInfo) {
        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AppInitialize.dataSource.let {
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
    fun broadCastMessage(data: MutableMap<String, String>) {
        if (data.get("who") != null) {
            val clientID =
                AppInitialize.dataSource.getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
            if (clientID == data.get("who")) {
                data.get("chatinfo_id").apply {
                    FirebaseMessaging.getInstance().subscribeToTopic(this)
                }
                sendBroadcast(Intent("updateChatList"))
                return
            }
        }

        if (data.get("chatinfo_id") != null) {
            data.get("chatinfo_id").apply {
                FirebaseMessaging.getInstance().subscribeToTopic(this)
            }
            return
        }
    }


}