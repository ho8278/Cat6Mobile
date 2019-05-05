package com.example.myapplication.data

import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.data.model.ServerResponse
import com.example.myapplication.data.model.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody

interface DataSource{
    fun insertUser(user:User)
    fun createTable()
    fun sendMessage(chatInfo: ChatInfo):Single<ResponseBody>
    fun receiveMessage(chatInfo:ChatInfo)
    fun receiveMessage(): Observable<ChatInfo>
    fun loadChatInfoList(roomId:String):Single<List<ChatInfo>>
    fun saveString(key:String,text:String)
    fun getString(key:String):String
    fun getCurrentUser():Single<User>
    fun loadSchedule(groupId:String):Completable
    fun getSchedules(year:Int,month:Int,day:Int):Single<List<Schedule>>
    fun saveSchedule(schedule: Schedule):Single<ServerResponse>
}