package com.example.myapplication.data

import com.example.myapplication.data.model.*
import com.example.myapplication.view.main.ErrorCode
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody

interface DataSource{
    fun sendMessage(chatInfo: ChatInfo):Single<ResponseBody>
    fun receiveMessage(chatInfo:ChatInfo)
    fun receiveMessage(): Observable<ChatInfo>

    fun loadChatInfoList(roomId:String):Single<List<ChatInfo>>

    fun <T:Any> saveItem(key:String,text:T)
    fun <T:Any> getItem(key:String):T

    fun getCurrentUser():Single<User>

    fun loadSchedule(groupId:String):Single<ErrorCode>
    fun getSchedules(year:Int,month:Int,day:Int):Single<List<Schedule>>
    fun saveSchedule(schedule: Schedule):Single<String>

    fun insertTeam(team: Team)
    fun loadTeam(userID:String):Single<List<Team>>

    fun login(id:String, pw:String): Single<ServerResponse<Team>>
    fun subscribeTopic(list:List<ChatRoom>)
    fun unSubscribeTopic(list:List<ChatRoom>)

    fun loadChatRoom():Single<List<ChatRoom>>

    fun createTeam(teamName:String):Single<String>

}