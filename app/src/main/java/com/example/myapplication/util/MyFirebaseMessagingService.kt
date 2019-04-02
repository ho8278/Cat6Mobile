package com.example.myapplication.util

import android.util.Log
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.model.User
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService(){

    private val TAG=MyFirebaseMessagingService::class.java.simpleName

    override fun onNewToken(token: String?) {
        Log.d(TAG,"Token: "+token)
        User("1","123","TestUser1","NickName",token ?: "NULL","")
            .let{
                DataManager.getInstance(applicationContext).insert(it)
                DataManager.getInstance(applicationContext).saveUserId(it.id)
            }
    }

    fun sendTokenToServer(token:String){
        //TODO:서버에 토큰값과 유저 정보 저장 및 ROOM에 저장
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.e("fff",remoteMessage.toString())
        remoteMessage?.data?.let{

        }
    }
}