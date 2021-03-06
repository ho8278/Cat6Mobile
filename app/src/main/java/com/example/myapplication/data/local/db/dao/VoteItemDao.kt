package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.VoteItem

@Dao
interface VoteItemDao{
    @Insert
    fun insertVoteItem(item:VoteItem)

    @Query("delete from VoteItem")
    fun deleteAll()

    @Query("select VoteItem.voteID from VoteItem where VoteItem.voteID = :id")
    fun getVoteID(id:String):String
}