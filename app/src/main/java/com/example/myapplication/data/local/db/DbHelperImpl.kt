package com.example.myapplication.data.local.db

import android.content.Context
import androidx.room.Room

class DbHelperImpl : DbHelper {
    companion object {
        private val dbName = "AppDatabase"

        @Volatile
        private var INSTANCE: DbHelperImpl? = null
        private lateinit var appDatabase: AppDatabase
        fun getInstance(context: Context): DbHelperImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, dbName).build()
                    DbHelperImpl()
                }
            }
    }
}