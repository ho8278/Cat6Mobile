package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.myapplication.data.model.Team

@Dao
interface TeamDao{
    @Insert
    fun insertTeam(team:Team)
}