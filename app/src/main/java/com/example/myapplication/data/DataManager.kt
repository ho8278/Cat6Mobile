package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.data.local.db.DbHelper
import com.example.myapplication.data.local.db.DbHelperImpl
import com.example.myapplication.data.remote.api.ApiHelper
import com.example.myapplication.data.remote.api.ApiHelperImpl

class DataManager : DataSource {

    private val apiHelper: ApiHelper =ApiHelperImpl.api

    companion object {
        private lateinit var dbHelper: DbHelper

        @Volatile
        private var INSTANCE: DataManager? = null

        fun getInstance(context: Context): DataManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    dbHelper = DbHelperImpl.getInstance(context)
                    DataManager()
                }
            }

    }
}