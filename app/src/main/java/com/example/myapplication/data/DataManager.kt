package com.example.myapplication.data

import android.content.Context
import android.util.Log
import com.example.myapplication.data.local.db.DbHelper
import com.example.myapplication.data.local.db.DbHelperImpl
import com.example.myapplication.data.local.pref.PreferenceHelper
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.*
import com.example.myapplication.data.remote.api.ApiHelper
import com.example.myapplication.data.remote.api.ApiHelperImpl
import com.example.myapplication.data.remote.fcm.FCMHelperImpl
import com.example.myapplication.view.main.ErrorCode
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.Response
import okhttp3.ResponseBody
import org.joda.time.format.DateTimeFormat
import org.json.JSONObject
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

    override fun insertUser(user: User) {
        dbHelper.insertUser(user)
    }

    override fun createTable() {
        //dbHelper.createChatTable("testest")
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun receiveMessage(chatInfo: ChatInfo) {
        receiveSubject.onNext(chatInfo)
    }

    override fun receiveMessage(): Observable<ChatInfo> {
        return receiveSubject.observeOn(AndroidSchedulers.mainThread())
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

    override fun loadSchedule(groupId: String): Completable {
        return apiHelper.loadSchedules(groupId).flatMapCompletable {
            dbHelper.insertScheduleList(it.data)
        }.subscribeOn(Schedulers.io())
    }

    override fun getSchedules(year: Int, month: Int, day: Int): Single<List<Schedule>> {
        return dbHelper.getSchedules(year, month, day)
    }

    override fun saveSchedule(schedule: Schedule): Single<Schedule> {
        val startDate = schedule.startDate
        val endDate = schedule.endDate

        if (startDate > endDate) {
            return Single.create<Schedule> {
                it.onError(Throwable(ErrorCode.LATE_START_DATE.code.toString()))
            }
        }

        if (schedule.name.isEmpty()) {
            return Single.create<Schedule> {
                it.onError(Throwable(ErrorCode.EMPTY_TEXT.code.toString()))
            }
        }
        val teamIdObserver = Single.fromCallable {
            prefHelper.getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        }
        dbHelper.insertSchedule(schedule)
        return Single.create {
            apiHelper.insertSchedule(startDate,endDate,schedule.name,schedule.teamID)
        }
    }

    override fun insertTeam(team: Team) {
        dbHelper.insertTeam(team)
    }

    override fun loadTeam(userID: String): Single<List<Team>> {
        return apiHelper.loadTeams(userID)
            .map { response -> response.data }
            .doOnSuccess { dbHelper.insertTeam(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadChatRoom(): Single<List<ChatRoom>> {
        return apiHelper.loadChatRooms(prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .map { response -> response.data }
            .doOnSuccess { dbHelper.insertChatRoomList(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(id:String, pw:String) {
        apiHelper.login(id,pw)
            .subscribeOn(Schedulers.io())
            .flatMap {
                prefHelper.saveItem(PreferenceHelperImpl.CURRENT_USER_ID, it.data[0].id)
                apiHelper.loadTeams(it.data[0].id)
            }
            .flatMap {
                prefHelper.saveItem(PreferenceHelperImpl.CURRENT_GROUP_ID, it.data[0].id)
                apiHelper.loadChatRooms(prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            }
            .subscribe({ it ->
                subscribeTopic(it.data)
                Log.e(TAG, it.toString())
            }, {
                Log.e(TAG, it.message)
            })
    }

    override fun subscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            list.forEach { subscribeToTopic(it.id) }
        }
    }

    override fun unSubscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            list.forEach { unsubscribeFromTopic(it.id) }
        }
    }
}