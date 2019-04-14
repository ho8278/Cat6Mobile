package com.example.myapplication.data.local.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DbHelperImpl : DbHelper {
    companion object {
        private val dbName = "AppDatabase"

        @Volatile
        private var INSTANCE: DbHelperImpl? = null
        private lateinit var appDatabase: AppDatabase
        fun getInstance(context: Context): DbHelperImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DbHelperImpl().apply {
                    appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()
                    INSTANCE=this
                }
            }
    }

    override fun loadChatInfoList(roomId: String): Single<List<ChatInfo>> {
        return Single.fromCallable {
            appDatabase.chatInfoDao.loadChatInfo(roomId)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun sendMessage(message: String) {
    }

    override fun insertUser(user: User) {
        Completable.fromAction {
            appDatabase.userDao.insertUser(user)
        }.subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun getUser(userId: String): Single<User> {
        return Single.fromCallable {
            appDatabase.userDao.getUser(userId)
        }.subscribeOn(Schedulers.io())
    }
}