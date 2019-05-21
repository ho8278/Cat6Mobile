package com.example.myapplication.data.local.db

import com.example.myapplication.data.model.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface DbHelper {
    fun sendMessage(message:String)

    fun loadChatInfoList(roomId:String): Single<List<ChatInfo>>
    fun insertChatInfo(info:ChatInfo)

    fun updateUser(user:User)
    fun getUser(userId:String):Single<User>

    fun insertSchedule(schedule:Schedule)
    fun updateSchedule(scheduleList:List<Schedule>)
    fun getSchedules(year:Int, month:Int, day:Int):Single<List<Schedule>>

    fun insertTeam(team: Team)
    fun updateTeam(listTeam:List<Team>)

    fun insertChatRoom(chatRoom: ChatRoom)
    fun updateChatRoom(list:List<ChatRoom>)
}