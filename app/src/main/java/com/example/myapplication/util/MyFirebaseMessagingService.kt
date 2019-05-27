package com.example.myapplication.util

import android.content.Intent
import android.util.Log
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = MyFirebaseMessagingService::class.java.simpleName

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Token: " + token)
    }

    fun sendTokenToServer(token: String) {
        //TODO:서버에 토큰값과 유저 정보 저장 및 ROOM에 저장
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val chatinfoID =
            DataManager.getInstance(applicationContext).getItem<String>(PreferenceHelperImpl.RECENT_CHATINFO_ID)

        val remoteInfoID = remoteMessage?.data?.get("chatinfo_id") ?: "null"
        if (chatinfoID == remoteInfoID)
            return

        if (remoteMessage?.from == "/topics/main") {
            broadCastMessage(remoteMessage.data)
            return
        }
    }

    fun broadCastMessage(data: MutableMap<String, String>) {
        if (data.get("who") != null) {
            val clientID =
                DataManager.getInstance(applicationContext).getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
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