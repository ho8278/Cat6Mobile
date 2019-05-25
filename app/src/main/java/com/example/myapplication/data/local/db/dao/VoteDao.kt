package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.model.Vote

@Dao
interface VoteDao{
    @Insert
    fun insertVote(vote:Vote)

    @Insert
    fun insertVoteList(list:List<Vote>)

    @Query("delete from vote")
    fun deleteAll()

    @Transaction
    fun updateVote(vote:Vote){
        deleteAll()
        insertVote(vote)
    }
}