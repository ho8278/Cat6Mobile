package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.myapplication.data.model.ChatInfo

@Dao
interface ChatInfoDao {
    @Insert
    fun insertChatInfo(chatinfo:ChatInfo)
}