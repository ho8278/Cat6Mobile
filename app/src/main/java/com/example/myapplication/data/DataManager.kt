package com.example.myapplication.data

import android.content.Context
import android.util.Log
import com.example.myapplication.data.local.db.DbHelper
import com.example.myapplication.data.local.db.DbHelperImpl
import com.example.myapplication.data.local.pref.PreferenceHelper
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.*
import com.example.myapplication.data.remote.api.ApiHelperImpl
import com.example.myapplication.data.remote.fcm.FCMHelperImpl
import com.example.myapplication.view.main.ErrorCode
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*

class DataManager : DataSource {

    private val apiHelper = ApiHelperImpl.api
    private val fcmApiHelper = FCMHelperImpl.api
    private val receiveSubject: Subject<ChatInfo>
    val TAG = DataManager::class.java.simpleName

    init {
        receiveSubject = PublishSubject.create()
        Log.e("DataManager", "Init")
    }

    companion object {
        private lateinit var dbHelper: DbHelper
        private lateinit var prefHelper: PreferenceHelper
        @Volatile
        private var INSTANCE: DataManager? = null

        fun getInstance(context: Context): DataManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataManager().apply {
                    dbHelper = DbHelperImpl.getInstance(context)
                    prefHelper = PreferenceHelperImpl.getInstance(context)
                    INSTANCE = this
                }
            }

    }

    override fun sendMessage(chatInfo: ChatInfo): Single<ResponseBody> {
        /*return fcmApiHelper.sendMessage(chatInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())*/
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/${chatInfo.roomId}"
            )
            addProperty("priority", "high")
            val element = JsonObject()
            element.addProperty("id", chatInfo.id)
            element.addProperty("message", chatInfo.message)
            element.addProperty("roomId", chatInfo.roomId)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.KOREA)
            element.addProperty("sendDate", dateFormat.format(chatInfo.sendDate))
            element.addProperty("sendId", chatInfo.sendUserId)
            add("data", element)
        }

        return fcmApiHelper.sendTestMessage(json)
            .doOnSuccess { dbHelper.insertChatInfo(chatInfo) }
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun sendBroadCastMessage(chatRoomID: String): Single<ResponseBody> {
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/main"
            )
            addProperty("priority", "high")
            val element = JsonObject()
            element.addProperty("id", chatRoomID)
            add("data", element)
        }
        return fcmApiHelper.sendTestMessage(json)
            .doOnError { Log.e(TAG, it.message) }
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
        dbHelper.insertChatInfo(chatInfo)
    }

    override fun loadChatInfoList(roomId: String): Single<List<ChatInfo>> {
        return dbHelper.loadChatInfoList(roomId)
    }

    override fun <T : Any> saveItem(key: String, text: T) {
        prefHelper.saveItem(key, text)
    }

    override fun <T : Any> getItem(key: String): T {
        return prefHelper.getItem(key)
    }

    override fun getCurrentUser(): Single<User> {
        return dbHelper.getUser(prefHelper.getItem(PreferenceHelperImpl.CURRENT_USER_ID))
    }

    override fun loadSchedule(groupId: String): Single<ErrorCode> {
        return apiHelper.loadSchedules(groupId)
            .doOnSuccess {
                Log.e(TAG, it.data.toString())
                dbHelper.updateSchedule(it.data)
            }
            .doOnError { Log.e(TAG, it.message) }
            .flatMap { it ->
                val code = it.responseCode.toInt()
                Single.create<ErrorCode> {
                    if (code == ErrorCode.WRONG_PARAMETER.code)
                        it.onError(Throwable(ErrorCode.WRONG_PARAMETER.description))
                    else
                        it.onSuccess(ErrorCode.SUCCESS)
                }
            }
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
    }

    override fun getSchedules(year: Int, month: Int, day: Int): Single<List<Schedule>> {
        return dbHelper.getSchedules(year, month, day)
    }

    override fun saveSchedule(schedule: Schedule): Single<String> {
        val startDate = schedule.startDate
        val endDate = schedule.endDate

        if (startDate > endDate) {
            return Single.create<String> {
                it.onError(Throwable(ErrorCode.LATE_START_DATE.code.toString()))
            }
        }

        if (schedule.name.isEmpty()) {
            return Single.create<String> {
                it.onError(Throwable(ErrorCode.EMPTY_TEXT.code.toString()))
            }
        }

        return apiHelper.insertSchedule(startDate, endDate, schedule.name, schedule.teamID)
            .doOnSuccess { it ->
                dbHelper.insertSchedule(
                    Schedule(
                        it,
                        schedule.startDate,
                        schedule.endDate,
                        schedule.name,
                        schedule.teamID
                    )
                )
            }
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertTeam(team: Team) {
        dbHelper.insertTeam(team)
    }

    override fun loadTeam(userID: String): Single<List<Team>> {
        return apiHelper.loadTeams(userID)
            .map { response -> response.data }
            .doOnError { Log.e(TAG, it.message) }
            .doOnSuccess { dbHelper.updateTeam(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadChatRoom(): Single<List<ChatRoom>> {
        return apiHelper.loadChatRooms(prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .map { response -> response.data }
            .doOnSuccess { dbHelper.updateChatRoom(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun createChatRoom(clientID: String, chatRoomName: String): Single<ChatRoom> {
        var chatID = ""
        return apiHelper.createChatRoom(chatRoomName, prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .subscribeOn(Schedulers.io())
            .doOnError { Log.e(TAG, it.message) }
            .flatMap {
                chatID = it
                apiHelper.inviteChatRoom(clientID, chatID)
            }
            .flatMap { sendBroadCastMessage(chatID) }
            .flatMap{Single.create<ChatRoom> { it.onSuccess(ChatRoom(chatID,chatRoomName)) }}
            .observeOn(AndroidSchedulers.mainThread())

    }

    override fun inviteChatRoom(clientID: String): Single<Int> {
        return apiHelper.inviteChatRoom(clientID, prefHelper.getItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID))
            .subscribeOn(Schedulers.io())

    }

    private fun sendBroadCastMessage(clientID:String, chatRoomID: String):Single<ResponseBody>{
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/main"
            )
            addProperty("priority", "high")
            val element = JsonObject()
            element.addProperty("id", chatRoomID)
            element.addProperty("who",clientID)
            add("data", element)
        }
        return fcmApiHelper.sendTestMessage(json)
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(id: String, pw: String): Single<ServerResponse<Team>> {
        return apiHelper.login(id, pw)
            .map { response ->
                val userData = response.data[0]
                userData.run {
                    User(this.id, password, name, nickname,profileLink)
                }
            }
            .doOnSuccess {
                prefHelper.saveItem(PreferenceHelperImpl.CURRENT_USER_ID, it.id)
                dbHelper.updateUser(it)
            }
            .subscribeOn(Schedulers.io())
            .flatMap { apiHelper.loadTeams(it.id) }
            .doOnSuccess { prefHelper.saveItem(PreferenceHelperImpl.CURRENT_GROUP_ID, it.data[0].id) }
    }

    override fun subscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            list.forEach { subscribeToTopic(it.id) }
            subscribeToTopic("main")
        }
    }

    override fun unSubscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            list.forEach { unsubscribeFromTopic(it.id) }
            unsubscribeFromTopic("main")
        }
    }

    override fun createTeam(teamName: String): Single<String> {
        return apiHelper.createTeam(teamName)
            .doOnError { Log.e(TAG, it.message) }
            .doOnSuccess { dbHelper.insertTeam(Team(it, teamName)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadGroupClient(): Single<List<User>> {
        return apiHelper.loadGroupClient(prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .map { response -> response.data }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setNotice(text: String,chatRoomID:String): Single<Int> {
        return apiHelper.setNotice(text, chatRoomID)
            .doOnSuccess {
                Log.e(TAG,it.toString())
            }
            .doOnError {
                Log.e(TAG,it.toString())
            }
            .flatMap {
                apiHelper.loadNotice(chatRoomID)
            }
            .doOnSuccess {
                Log.e(TAG,it.toString())
            }
            .doOnError {
                Log.e(TAG,it.toString())
            }
            .flatMap {
                if(it.responseCode.toInt() == ErrorCode.SUCCESS.code){
                    dbHelper.insertNotice(it.data[0])
                    Single.create<Int>{
                        it.onSuccess(ErrorCode.SUCCESS.code)
                    }
                }else{
                    Single.create<Int>{
                        it.onError(Throwable(ErrorCode.WRONG_PARAMETER.description))
                    }
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun loadNotice(chatRoomID: String): Single<Notice> {
        return apiHelper.loadNotice(chatRoomID)
            .map { response -> response.data[0] }
            .subscribeOn(Schedulers.io())
    }
}