package com.example.myapplication.data.local.db.dao

import androidx.room.*
import com.example.myapplication.data.model.Vote

@Dao
interface VoteDao{
    @Insert
    fun insertVote(vote:Vote)

    @Insert
    fun insertVoteList(list:List<Vote>)

    @Query("delete from vote")
    fun deleteAll()

    @Query("select * from Vote where id=:id")
    fun getVote(id:String):Vote

    @Update
    fun setVote(vote:Vote)

    @Transaction
    fun updateVote(vote:Vote){
        deleteAll()
        insertVote(vote)
    }
}