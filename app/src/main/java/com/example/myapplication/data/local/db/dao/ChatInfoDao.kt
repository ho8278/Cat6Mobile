package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.ChatInfo

@Dao
interface ChatInfoDao {
    @Insert
    fun insertChatInfo(chatinfo:ChatInfo)

    @Query("select * from chatinfo where roomId=:id")
    fun loadChatInfo(id:String) : List<ChatInfo>
}