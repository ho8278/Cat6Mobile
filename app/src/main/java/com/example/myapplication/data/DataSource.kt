package com.example.myapplication.data

import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.User
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody

interface DataSource{
    fun insert(user:User)
    fun createTable()
    fun sendMessage(chatInfo: ChatInfo):Single<ResponseBody>
    fun receiveMessage(chatInfo:ChatInfo)
    fun receiveMessage(): Observable<ChatInfo>
    fun loadChatInfoList(roomId:String):Single<List<ChatInfo>>
    fun saveString(key:String,text:String)
    fun getString(key:String):String
    //fun getUser(userId:String):Single<User>
    fun getCurrentUser():Single<User>
}