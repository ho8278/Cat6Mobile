package com.example.myapplication.data.local.db

import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.data.model.Team
import com.example.myapplication.data.model.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface DbHelper {
    fun sendMessage(message:String)

    fun loadChatInfoList(roomId:String): Single<List<ChatInfo>>

    fun insertUser(user:User)

    fun getUser(userId:String):Single<User>

    fun insertSchedule(schedule:Schedule)

    fun insertScheduleList(scheduleList:List<Schedule>):Completable

    fun deleteAllSchedules():Completable

    fun getSchedules(year:Int, month:Int, day:Int):Single<List<Schedule>>

    fun insertTeam(team: Team)
}