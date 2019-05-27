package com.example.myapplication.data.local.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myapplication.data.model.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DbHelperImpl : DbHelper {
    private val TAG = DbHelperImpl::class.java.simpleName

    companion object {
        private val dbName = "AppDatabase"

        @Volatile
        private var INSTANCE: DbHelperImpl? = null
        private lateinit var appDatabase: AppDatabase
        fun getInstance(context: Context): DbHelperImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DbHelperImpl().apply {
                    appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()
                    INSTANCE = this
                }
            }
    }

    override fun loadChatInfoList(roomId: String): Single<List<ChatInfo>> {
        return Single.fromCallable {
            appDatabase.chatInfoDao.loadChatInfo(roomId)
        }.subscribeOn(Schedulers.io())
            .doOnError { Log.e(TAG, it.message) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertChatInfo(info: ChatInfo) {
        Completable.fromAction {
            appDatabase.chatInfoDao.insertChatInfo(info)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun sendMessage(message: String) {
    }

    override fun updateUser(user: User) {
        Completable.fromAction {
            appDatabase.userDao.updateUser(user)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun updateUser(list: List<User>) {
        Completable.fromAction {
            appDatabase.userDao.updateUser(list)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun getUser(userId: String): Single<User> {
        return Single.fromCallable {
            appDatabase.userDao.getUser(userId)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Log.e(TAG, it.message) }
    }

    override fun updateSchedule(scheduleList: List<Schedule>) {
        Completable.fromAction {
            appDatabase.scheduleDao.updateSchedule(scheduleList)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun updateSchedule(schedule: Schedule) {
        Completable.fromAction {
            appDatabase.scheduleDao.updateSchedule(schedule)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun getSchedules(year: Int, month: Int, day: Int): Single<List<Schedule>> {
        val realMonth = if (month < 10)
            "0" + month
        else
            month

        val realDay = if(day < 10)
            "0"+day
        else
            day
        val date = "$year-$realMonth-$realDay%"
        Log.e(TAG,date)
        return Single.fromCallable {
            appDatabase.scheduleDao.getSchedules(date)
        }.doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertSchedule(schedule: Schedule) {
        Completable.fromAction {
            appDatabase.scheduleDao.insertSchedules(schedule)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun insertTeam(team: Team) {
        Completable.fromAction {
            appDatabase.teamDao.insertTeam(team)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun updateTeam(listTeam: List<Team>) {
        Completable.fromAction {
            appDatabase.teamDao.updateTeamList(listTeam)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun updateChatRoom(list: List<ChatRoom>) {
        Completable.fromAction {
            appDatabase.chatDao.updateChatRoom(list)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }


    override fun insertChatRoom(chatRoom: ChatRoom) {
        Completable.fromAction {
            appDatabase.chatDao.insertChatRoom(chatRoom)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{ Log.e(TAG, it.message) })
    }

    override fun insertNotice(notice: Notice) {
        Completable.fromAction {
            appDatabase.noticeDao.updateNotice(notice)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{Log.e(TAG,it.message)})
    }

    override fun insertVote(vote: Vote) {
        Completable.fromAction {
            appDatabase.voteDao.insertVote(vote)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{Log.e(TAG,it.message)})
    }

    override fun insertVoteList(list: List<Vote>) {
        Completable.fromAction {
            appDatabase.voteDao.insertVoteList(list)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{Log.e(TAG,it.message)})
    }

    override fun updateVote(vote: Vote) {
        Completable.fromAction {
            appDatabase.voteDao.updateVote(vote)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{Log.e(TAG,it.message)})
    }

    override fun insertVoteItem(voteItem: VoteItem) {
        Completable.fromAction {
            appDatabase.voteItemDao.insertVoteItem(voteItem)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{Log.e(TAG,it.message)})
    }

    override fun setVote(vote: Vote) {
        Completable.fromAction {
            appDatabase.voteDao.setVote(vote)
        }.subscribeOn(Schedulers.io())
            .subscribe({},{Log.e(TAG,it.message)})
    }

    override fun getVote(id: String): Single<Vote> {
        return Single.create<Vote> {
            it.onSuccess(appDatabase.voteDao.getVote(id))
        }.subscribeOn(Schedulers.io())
    }

    override fun getVoteID(id: String): Single<String> {
        return Single.create<String> {
            it.onSuccess(appDatabase.voteItemDao.getVoteID(id))
        }.subscribeOn(Schedulers.io())
    }
}