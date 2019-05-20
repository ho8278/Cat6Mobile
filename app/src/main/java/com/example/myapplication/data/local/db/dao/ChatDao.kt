package com.example.myapplication.data.local.db.dao

import androidx.room.*
import com.example.myapplication.data.model.ChatRoom


@Dao
interface ChatDao{
    @Insert
    fun insertChatRoom(chatRoom: ChatRoom)

    @Insert
    fun insertChatRoom(list: List<ChatRoom>)

    @Query("delete from ChatRoom")
    fun deleteChatRoom()

    @Transaction
    fun updateChatRoom(list:List<ChatRoom>){
        deleteChatRoom()
        insertChatRoom(list)
    }

}