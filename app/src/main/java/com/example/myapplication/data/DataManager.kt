package com.example.myapplication.data

import android.content.Context
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
import okhttp3.Response
import okhttp3.ResponseBody

class DataManager: DataSource {

    private val apiHelper = ApiHelperImpl.api
    private val fcmApiHelper = FCMHelperImpl.api
    private val receiveSubject = PublishSubject.create<ChatInfo>()

    companion object {
        private lateinit var dbHelper: DbHelper
        private lateinit var prefHelper:PreferenceHelper
        @Volatile
        private var INSTANCE: DataManager? = null

        fun getInstance(context: Context): DataManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    dbHelper = DbHelperImpl.getInstance(context)
                    prefHelper= PreferenceHelperImpl.getInstance(context)
                    DataManager()
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
            addProperty("to","eP6qivefzZs:APA91bGraQmqJ2JQykq8mAs-ROPxqxcs3r7CNFJvceTjRzuGl2j0hL3CUwOGZWzzkB6lUVhaji2hIVRaxH0tiWtSl8E6DEPcJTwOSx0m8Od1XWe5-CnoayFSetBJHrnLMPP19KT6XW0f")
            addProperty("priority","high")
            val element = JsonObject()
            element.addProperty("ttt","testtest")
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

    /*override fun getUser(userId:String): Single<User> {
        return dbHelper.getUser(userId)
    }*/
    override fun saveUserId(userId: String) {
        prefHelper.saveUserId(userId)
    }

    override fun getUser(): Single<User> {
        return dbHelper.getUser(prefHelper.getUserId())
    }
}