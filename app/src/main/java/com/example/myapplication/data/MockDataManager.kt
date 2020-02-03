package com.example.myapplication.data

import android.content.Context
import android.util.Log
import com.example.myapplication.data.local.pref.PreferenceHelper
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.*
import com.example.myapplication.data.remote.fcm.FCMHelperImpl
import com.example.myapplication.view.main.ErrorCode
import com.example.myapplication.view.references.Reference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MockDataManager : DataSource {


    private val fcmApiHelper = FCMHelperImpl.api
    private val database = FirebaseDatabase.getInstance().reference
    private val receiveSubject: Subject<ChatInfo>
    val TAG = MockDataManager::class.java.simpleName

    init {
        receiveSubject = PublishSubject.create()
    }

    companion object {
        private lateinit var prefHelper: PreferenceHelper
        @Volatile
        private var INSTANCE: MockDataManager? = null

        fun getInstance(context: Context): MockDataManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MockDataManager().apply {
                    prefHelper = PreferenceHelperImpl.getInstance(context)
                    INSTANCE = this
                }
            }

    }


    override fun sendMessage(chatInfo: ChatInfo): Single<ResponseBody> {
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/${chatInfo.chatroom_id}"
            )
            addProperty("priority", "high")
            val chatInfoJson = JsonObject().apply {
                addProperty("chatinfo_id", chatInfo.chatinfo_id)
                addProperty("message", chatInfo.message)
                addProperty("chatroom_id", chatInfo.chatroom_id)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                addProperty("send_date", dateFormat.format(chatInfo.send_date))
                addProperty("send_user_id", chatInfo.send_user_id)
            }
            add("data", chatInfoJson)
        }
        saveItem(PreferenceHelperImpl.RECENT_CHATINFO_ID, chatInfo.chatinfo_id)
        return fcmApiHelper.sendTestMessage(json)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun receiveMessage(chatInfo: ChatInfo) {
        receiveSubject.onNext(chatInfo)
    }

    override fun receiveMessage(): Observable<ChatInfo> {
        return receiveSubject.observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertChatInfo(chatInfo: ChatInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadChatInfoList(roomId: String): Single<List<ChatInfo>> {
        val currentTeamId = getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        val chatInfoDatabase = database.child("teams/$currentTeamId/chatrooms/$roomId/chatinfo")

        return Single.create {
            chatInfoDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val chatInfoList = p0.children.map {
                        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                        ChatInfo(
                            it.key ?: UUID.randomUUID().toString(),
                            it.child("send_user_id").value as String,
                            roomId,
                            date.parse(it.child("send_date").value as String),
                            it.child("message").value as String
                        )
                    }
                    it.onSuccess(chatInfoList)
                }
            })
        }
    }

    override fun <T : Any> saveItem(key: String, text: T) {
        prefHelper.saveItem(key, text)
    }

    override fun <T : Any> getItem(key: String): T {
        return prefHelper.getItem(key)
    }

    override fun loadUser(userID: String): Single<UserServerResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentUser(): Single<User> {
        return getUser(getItem(PreferenceHelperImpl.CURRENT_USER_ID))
    }

    override fun getUser(userID: String): Single<User> {
        val userDatabase = database.child("users")
        return Single.create {
            userDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.children.find { it.key.equals(userID) }
                    if (user != null) {
                        val findUser = User(
                            user.key ?: UUID.randomUUID().toString(),
                            user.child("password").value as String,
                            user.child("name").value as String,
                            user.child("nickname").value as String
                        )
                        it.onSuccess(findUser)
                    }
                }
            })
        }
    }

    override fun loadSchedule(groupId: String): Single<List<Schedule>> {
        val currentTeamID = getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        val scheduleDatabase = database.child("teams/$currentTeamID/schedules")
        return Single.create {
            scheduleDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val scheduleList = p0.children.map {
                        Schedule(
                            it.key ?: UUID.randomUUID().toString(),
                            it.child("start_date").value as String,
                            it.child("end_date").value as String,
                            it.child("name").value as String,
                            currentTeamID
                        )
                    }
                    it.onSuccess(scheduleList)
                }
            })
        }
    }

    override fun getSchedules(year: Int, month: Int, day: Int): Single<List<Schedule>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveSchedule(schedule: Schedule): Single<String> {
        val currentTeamID = getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        val scheduleDatabase = database.child("teams/$currentTeamID/schedules")
        return Single.create {
            val emitter = it
            val map = mapOf("start_date" to schedule.startDate, "end_date" to schedule.endDate, "name" to schedule.name)
            scheduleDatabase.push().setValue(map)
                .addOnSuccessListener {
                    emitter.onSuccess("ㄱㄱ")
                }
        }
    }

    override fun deleteSchedule(id: String): Single<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSchedulers(schedule: Schedule) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertTeam(team: Team) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTeam(userID: String): Single<List<Team>> {
        val teamDatabase = database.child("teams")
        return Single.create {
            teamDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val teamList = p0.children.map {
                        Team(it.key ?: UUID.randomUUID().toString(), it.child("name").value as String)
                    }
                    it.onSuccess(teamList)
                }
            })
        }
    }

    override fun addUserToTeam(userID: String): Single<ErrorCode> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTeam(teamID: String): Single<Team> {
        val teamDatabase = database.child("teams")
        return Single.create {
            teamDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val team = p0.children.find { it.key == teamID }
                    it.onSuccess(Team(teamID, team?.child("name")?.value as String))
                }
            })
        }
    }

    override fun login(id: String, pw: String): Single<ServerResponse<Team>> {
        val userDatabase = database.child("users")
        return Single.create {
            userDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    it.onError(Throwable())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.children.find { it.key.equals(id) && pw.equals(it.child("password").value as String) }
                    if (user != null) {
                        saveItem(PreferenceHelperImpl.CURRENT_USER_ID, id)
                        val teamList = user.child("teams").children.map {
                            Team(
                                it.key ?: UUID.randomUUID().toString(),
                                it.value as String
                            )
                        }
                        if (!teamList.isEmpty())
                            saveItem(PreferenceHelperImpl.CURRENT_GROUP_ID, teamList[0].id)
                        it.onSuccess(ServerResponse("200", teamList))
                    }
                }
            })
        }
    }

    override fun logout(): Single<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun subscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            subscribeToTopic("main")
            list.forEach {
                subscribeToTopic(it.id)
            }
        }
    }

    override fun unSubscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            unsubscribeFromTopic("main")
            list.forEach {
                unsubscribeFromTopic(it.id)
            }
        }
    }

    override fun loadChatRoom(): Single<List<ChatRoom>> {
        val currentTeamId = getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        val chatRoomDatabase = database.child("teams/$currentTeamId/chatrooms")
        return Single.create {
            chatRoomDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val currentUser = getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
                    val chatRoomList = p0.children.filter {
                        !it.child("users").children.filter { it.key.equals(currentUser) }.isEmpty()
                    }.map {
                        ChatRoom(
                            it.key ?: UUID.randomUUID().toString(),
                            it.child("name").value as String
                        )
                    }
                    if (!chatRoomList.isEmpty())
                        saveItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID, chatRoomList[0].id)
                    it.onSuccess(chatRoomList)
                }
            })
        }
    }

    override fun createChatRoom(clientID: String, chatRoomName: String): Single<ChatRoom> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun inviteChatRoom(clientID: String): Single<Int> {
        return sendBroadCastMessage(clientID, getItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID))
            .map { response -> 1 }
            .subscribeOn(Schedulers.io())
    }

    private fun sendBroadCastMessage(clientID: String, chatRoomID: String): Single<ResponseBody> {
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/main"
            )
            addProperty("priority", "high")
            val element = JsonObject()
            element.addProperty("chatinfo_id", chatRoomID)
            element.addProperty("who", clientID)
            add("data", element)
        }
        return fcmApiHelper.sendTestMessage(json)
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun createTeam(teamName: String): Single<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendBroadCastMessage(chatRoomID: String): Single<ResponseBody> {
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/main"
            )
            addProperty("priority", "high")
            val element = JsonObject()
            element.addProperty("chatinfo_id", chatRoomID)
            add("data", element)
        }
        return fcmApiHelper.sendTestMessage(json)
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadGroupClient(): Single<List<User>> {
        val currentTeamdId = getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        val teamDatabase = database.child("teams/$currentTeamdId/users")
        return Single.create {
            teamDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val currentUserId = getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
                    val userList = p0.children.map {
                        User(it.key ?: UUID.randomUUID().toString(), "", "", it.value as String)
                    }.filter { it.id != currentUserId }
                    it.onSuccess(userList)
                }
            })
        }
    }

    override fun loadChatClient(): Single<List<String>> {
        val currentChatRoomID = getItem<String>(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID)
        val currentGroupID = getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        val teamDatabase = database.child("teams/$currentGroupID/chatrooms/$currentChatRoomID/users")
        return Single.create {
            teamDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val userList = p0.children.map { it.value as String }
                    it.onSuccess(userList)
                }
            })
        }
    }

    override fun uploadFile(path: String): Single<Int> {
        return Single.create { it.onSuccess(1) }
    }

    override fun setNotice(text: String, chatRoomID: String): Single<Int> {
        return Single.create { it.onSuccess(1) }
    }

    override fun loadNotice(chatRoomID: String): Single<Notice> {
        return Single.create { it.onSuccess(Notice("1", "", "")) }
    }

    override fun createVote(vote: Vote, list: MutableList<String>): Observable<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createVoteItem(name: String, id: String): Single<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadVote(): Single<List<Vote>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadDetailVote(voteID: String): Single<Vote> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun acceptVote(voteID: String, voteItemIDlist: List<String>): Observable<ErrorCode> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadReferences(): Observable<ServerResponse<Reference>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uploadFile(list: MutableList<MultipartBody.Part>): Observable<ServerResponse<com.example.myapplication.data.model.File>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun downloadFile(fileName: String): Single<File> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun join(id: String, pw: String, name: String, nickname: String): Single<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}