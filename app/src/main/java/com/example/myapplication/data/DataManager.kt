package com.example.myapplication.data

import android.content.Context
import android.util.Log
import com.example.myapplication.data.local.db.DbHelper
import com.example.myapplication.data.local.db.DbHelperImpl
import com.example.myapplication.data.local.pref.PreferenceHelper
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.User
import com.example.myapplication.data.remote.api.ApiHelper
import com.example.myapplication.data.remote.api.ApiHelperImpl
import com.example.myapplication.data.remote.fcm.FCMHelperImpl
import com.google.gson.JsonObject
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.Response
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*

class DataManager: DataSource {

    private val apiHelper = ApiHelperImpl.api
    private val fcmApiHelper = FCMHelperImpl.api
    private val receiveSubject:Subject<ChatInfo>

    init{
        receiveSubject=PublishSubject.create()
        Log.e("DataManager","Init")
    }

    companion object {
        private lateinit var dbHelper: DbHelper
        private lateinit var prefHelper:PreferenceHelper
        @Volatile
        private var INSTANCE: DataManager? = null

        fun getInstance(context: Context): DataManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataManager().apply {
                    dbHelper = DbHelperImpl.getInstance(context)
                    prefHelper= PreferenceHelperImpl.getInstance(context)
                    INSTANCE=this
                }
            }

    }

    override fun insert(user:User) {
        dbHelper.insertUser(user)
    }

    override fun createTable() {
        //dbHelper.createChatTable("testest")
    }

    override fun sendMessage(chatInfo: ChatInfo): Single<ResponseBody> {
        /*return fcmApiHelper.sendMessage(chatInfo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())*/
        val json = JsonObject().apply{
            addProperty("to","cFb4b2Weff8:APA91bHqnjHOGgEuILkv9wBV96zVBA6WDc0zotuDDjBS7prwb5Poc-Y1md6bNVGibr8xZ-WvdWJkSOfXzhQRlWg-o2BUNjjoDPV1aeJGcLgt5OGklIxT1M52WxitiPYamp883gelZPEM")
            addProperty("priority","high")
            val element = JsonObject()
            element.addProperty("message",chatInfo.message)
            element.addProperty("roomId",chatInfo.roomId)
            val dateFormat=SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.KOREA)
            element.addProperty("sendDate",dateFormat.format(chatInfo.sendDate))
            element.addProperty("sendId",chatInfo.sendUserId)
            add("data",element)
        }

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

    override fun loadChatInfoList(roomId:String): Single<List<ChatInfo>> {
        return dbHelper.loadChatInfoList(roomId)
    }

    /*override fun getUser(userId:String): Single<User> {
            return dbHelper.getUser(userId)
        }*/
    override fun saveString(key:String,text: String) {
        prefHelper.saveString(key,text)
    }

    override fun getString(key: String): String {
        return prefHelper.getString(key)
    }

    override fun getCurrentUser(): Single<User> {
        return dbHelper.getUser(prefHelper.getString(PreferenceHelperImpl.CURRENT_USER_ID))
    }
}