package com.example.myapplication.data.local.db

import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.User
import io.reactivex.Maybe
import io.reactivex.Single

interface DbHelper {
    fun sendMessage(message:String)

    //fun getChatRoomMessages(chatid:String): Single<List<ChatInfo>>

    fun insertUser(user:User)

    fun getUser(userId:String):Single<User>
}