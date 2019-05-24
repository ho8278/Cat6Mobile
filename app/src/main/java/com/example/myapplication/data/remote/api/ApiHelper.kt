package com.example.myapplication.data.remote.api

import com.example.myapplication.data.model.*
import io.reactivex.Single
import retrofit2.http.*
import java.util.*

interface ApiHelper {
    @GET("viewSchedules")
    fun loadSchedules(@Query("team_ID") groupId: String): Single<ServerResponse<Schedule>>

    @GET("viewTeams")
    fun loadTeams(@Query("client_ID") userId: String): Single<ServerResponse<Team>>

    @GET("showClientInfo")
    fun getUser(@Query("client_ID") userId: String): Single<ServerResponse<User>>

    @GET("login")
    fun login(@Query("client_ID") id: String, @Query("client_password") pw: String): Single<ServerResponse<User>>

    @GET("viewChatRooms")
    fun loadChatRooms(@Query("team_ID") teamId: String): Single<ServerResponse<ChatRoom>>

    @POST("setSchedule")
    fun insertSchedule(
        @Query("schedule_start_date") startDate: String, @Query("schedule_end_date") endDate: String,
        @Query("schedule_contents") contents: String, @Query("schedule_team_ID") teamID: String
    ): Single<String>

    @POST("createTeam")
    fun createTeam(@Query("team_name") teamName: String): Single<String>

    @GET("viewParticipateClients/team")
    fun loadGroupClient(@Query("team_ID") teamID: String): Single<ServerResponse<User>>

    @POST("createChatRoom")
    fun createChatRoom(@Query("chat_room_name") name: String, @Query("team_ID") teamID: String): Single<String>

    @POST("inviteChatRoom")
    fun inviteChatRoom(@Query("client_ID") clientID: String, @Query("chat_room_ID") chatID: String): Single<Int>

    @POST("notice")
    fun setNotice(@Query("notice_contents")contents:String, @Query("chat_room_ID")chatID:String):Single<Int>

    @GET("viewNotice")
    fun loadNotice(@Query("chat_room_ID")chatRoomID:String):Single<ServerResponse<Notice>>
}