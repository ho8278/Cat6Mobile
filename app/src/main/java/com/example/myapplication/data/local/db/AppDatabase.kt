package com.example.myapplication.data.local.db

import androidx.room.RoomDatabase
import com.example.myapplication.data.local.db.dao.*

abstract class AppDatabase:RoomDatabase(){
    abstract val userDao:UserDao
    abstract val chatDao:ChatDao
    abstract val fileDao:FileDao
    abstract val noticeDao:NoticeDao
    abstract val teamDao:TeamDao
    abstract val voteDao:VoteDao
}