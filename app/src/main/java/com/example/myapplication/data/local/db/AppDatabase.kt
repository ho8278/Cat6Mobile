package com.example.myapplication.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.local.db.dao.*
import com.example.myapplication.data.model.*
import com.example.myapplication.util.DateTypeConverter

@Database(entities= [
    ChatRoom::class,
    File::class,
    Notice::class,
    Schedule::class,
    Team::class,
    User::class,
    Vote::class,
    VoteItem::class,
    ChatInfo::class
],version = 1)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase:RoomDatabase(){
    abstract val userDao:UserDao
    abstract val chatDao:ChatDao
    abstract val fileDao:FileDao
    abstract val chatInfoDao:ChatInfoDao
    abstract val noticeDao:NoticeDao
    abstract val scheduleDao:ScheduleDao
    abstract val teamDao:TeamDao
    abstract val voteDao:VoteDao
}