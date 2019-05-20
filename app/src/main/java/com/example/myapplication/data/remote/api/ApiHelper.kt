package com.example.myapplication.data.remote.api

import com.example.myapplication.data.model.*
import io.reactivex.Single
import retrofit2.http.*

interface ApiHelper {
    @GET("schedule")
    fun loadSchedules(@Query("groupid") groupId: String): Single<ServerResponse<Schedule>>

    @GET("viewTeams")
    fun loadTeams(@Query("client_ID") userId: String): Single<ServerResponse<Team>>

    @GET("login")
    fun login(@Query("client_ID")id:String, @Query("client_password")pw:String):Single<ServerResponse<User>>

    @GET("chatroom")
    fun loadChatRooms(@Query("userid") userId: String, @Query("teamid") teamId: String): Single<ServerResponse<ChatRoom>>

    @PUT("schedule")
    fun insertSchedule(@Body schedule:Schedule):Single<ServerResponse<Schedule>>
}