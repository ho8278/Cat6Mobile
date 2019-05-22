package com.example.myapplication.data

import android.net.Uri
import com.example.myapplication.data.model.*
import com.example.myapplication.view.main.ErrorCode
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.data.model.Team
import com.example.myapplication.data.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody

interface DataSource{
    fun sendMessage(chatInfo: ChatInfo)
    fun receiveMessage(chatInfo:ChatInfo)
    fun receiveMessage(): Observable<ChatInfo>

    fun insertChatInfo(chatInfo:ChatInfo)
    fun loadChatInfoList(roomId:String):Single<List<ChatInfo>>

    fun <T:Any> saveItem(key:String,text:T)
    fun <T:Any> getItem(key:String):T

    fun getCurrentUser():Single<User>
    fun getUser(userID:String):Single<User>

    fun loadSchedule(groupId:String):Single<ErrorCode>
    fun getSchedules(year:Int,month:Int,day:Int):Single<List<Schedule>>
    fun saveSchedule(schedule: Schedule):Single<String>

    fun insertTeam(team: Team)
    fun loadTeam(userID:String):Single<List<Team>>

    fun login(id:String, pw:String): Single<ServerResponse<Team>>
    fun subscribeTopic(list:List<ChatRoom>)
    fun unSubscribeTopic(list:List<ChatRoom>)

    fun loadChatRoom():Single<List<ChatRoom>>
    fun createChatRoom(clientID:String, chatRoomName:String):Single<ChatRoom>
    fun inviteChatRoom(clientID:String):Single<Int>

    fun createTeam(teamName:String):Single<String>

    fun sendBroadCastMessage(chatRoomID:String):Single<ResponseBody>

    fun loadGroupClient():Single<List<User>>

    fun uploadFile(path: String, chatInfo:ChatInfo): Single<Int>

    fun setNotice(text:String,chatRoomID:String):Single<Int>
    fun loadNotice(chatRoomID:String):Single<Notice>

    fun createVote(vote:Vote, list:MutableList<String>):Observable<String>
    fun createVoteItem(name:String, id:String):Single<Int>
    fun loadVote():Single<List<Vote>>
    fun loadDetailVote(voteID:String):Single<Vote>
    fun acceptVote(voteID:String,voteItemIDlist:List<String>):Observable<ErrorCode>

    fun join(id: String,pw: String, name: String, nickname: String):Single<Int>
}