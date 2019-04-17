package com.example.myapplication.data.remote.api

import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.data.model.Team
import io.reactivex.Single
import retrofit2.http.*

interface ApiHelper {
    @GET("schedule")
    fun loadSchedules(@Query("groupid") groupId: String): Single<List<Schedule>>

    @GET("team")
    fun loadTeams(@Query("userid") userId: String): Single<List<Team>>

    @GET("chatroom")
    fun loadChatRooms(@Query("userid") userId: String, @Query("teamid") teamId: String): Single<List<ChatRoom>>

    @PUT("schedule")
    fun insertSchedule(@Body schedule:Schedule):Single<String>
}