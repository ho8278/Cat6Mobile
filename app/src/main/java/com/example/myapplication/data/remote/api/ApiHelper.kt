package com.example.myapplication.data.remote.api

import com.example.myapplication.data.model.*
import io.reactivex.Single
import retrofit2.http.*
import java.util.*

interface ApiHelper {
    @GET("viewSchedules")
    fun loadSchedules(@Query("groupid") groupId: String): Single<ServerResponse<Schedule>>

    @GET("viewTeams")
    fun loadTeams(@Query("client_ID") userId: String): Single<ServerResponse<Team>>

    @GET("showClientInfo")
    fun getUser(@Query("client_ID") userId: String):Single<ServerResponse<User>>

    @GET("login")
    fun login(@Query("client_ID")id:String, @Query("client_password")pw:String):Single<ServerResponse<User>>

    @GET("viewChatRooms")
    fun loadChatRooms(@Query("team_ID") teamId: String): Single<ServerResponse<ChatRoom>>

    @POST("setSchedule")
    fun insertSchedule(@Query("schedule_start_date") startDate: String, @Query("schedule_end_date")endDate:String,
                       @Query("schedule_contents") contents:String, @Query("schedule_team_ID")teamID:String)
    @POST("schedule")
    fun insertSchedule(@Body schedule:Schedule):Single<ServerResponse<Schedule>>

    @GET("createTeam")
    fun createTeam(@Query("team_name")teamName:String):Single<ServerResponse<Team>>
}