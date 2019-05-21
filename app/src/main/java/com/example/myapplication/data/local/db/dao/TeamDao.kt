package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.model.Team

@Dao
interface TeamDao{
    @Insert
    fun insertTeam(team:Team)

    @Insert
    fun insertTeam(listTeam:List<Team>)

    @Query("delete from Team")
    fun deleteTeam()

    @Transaction
    fun updateTeamList(listTeam:List<Team>){
        deleteTeam()
        insertTeam(listTeam)
    }
}