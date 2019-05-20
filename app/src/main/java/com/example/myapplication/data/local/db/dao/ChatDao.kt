package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.myapplication.data.model.ChatRoom


@Dao
interface ChatDao{
    @Insert
    fun insertChatRoom(chatRoom: ChatRoom)

    @Insert
    fun insertChatRoom(list: List<ChatRoom>)
}