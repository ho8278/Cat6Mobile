package com.example.myapplication.data.remote.api

import com.example.myapplication.data.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*

interface ApiHelper {
    @GET("viewSchedules")
    fun loadSchedules(@Query("team_ID") groupId: String): Single<ServerResponse<Schedule>>

    @GET("viewTeams")
    fun loadTeams(@Query("client_ID") userId: String): Single<ServerResponse<Team>>

    @GET("showClientInfo")
    fun getUser(@Query("client_ID") userId: String): Single<UserServerResponse>

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

    @POST("inviteTeam")
    fun inviteTeam(@Query("client_ID")clientID:String, @Query("team_ID")teamID:String):Single<Int>

    @Multipart
    @POST("uploadFile")
    fun uploadFile(@Part("team_ID")teamID:RequestBody, @Part file:MultipartBody.Part):Single<ServerResponse<File>>

    @POST("notice")
    fun setNotice(@Query("notice_contents") contents: String, @Query("chat_room_ID") chatID: String): Single<Int>

    @GET("viewNotice")
    fun loadNotice(@Query("chat_room_ID") chatRoomID: String): Single<ServerResponse<Notice>>

    @POST("createVote")
    fun createVote(@Query("vote_title") title: String, @Query("vote_start_date") startDate: String, @Query("vote_end_date") endDate: String,
        @Query("vote_duplicate") duplicate: String, @Query("chat_room_ID") roomID: String): Single<String>

    @POST("createVoteItem")
    fun createVoteItem(@Query("vote_item_name")name:String, @Query("vote_ID")voteID:String):Single<String>

    @GET("viewVotes")
    fun loadVotes(@Query("chat_room_ID")chatRoomID:String):Single<ServerResponse<Vote>>

    @GET("viewVoteInfo")
    fun loadDetailVote(@Query("vote_ID")voteID:String):Single<VoteServerResponse>

    @POST("vote")
    fun vote(@Query("vote_ID")voteID:String,@Query("vote_item_ID")voteItemID:String,@Query("client_ID")clientID:String):Single<Int>

    @GET("checkVote")
    fun checkVote(@Query("vote_ID")voteID:String):Single<ServerResponse<User>>
    fun insertSchedule(
        @Query("schedule_start_date") startDate: String, @Query("schedule_end_date") endDate: String,
        @Query("schedule_contents") contents: String, @Query("schedule_team_ID") teamID: String
    )

    @POST("createClient")
    fun join(
        @Query("client_ID") client_ID: String, @Query("client_password") client_password: String, @Query("client_name") client_name: String, @Query(
            "client_nickname") client_nickname: String,@Query("profileLink")link:String=""
    ): Single<Int>
}