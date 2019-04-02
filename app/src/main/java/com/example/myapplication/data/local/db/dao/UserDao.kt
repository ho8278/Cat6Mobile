package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.User
import io.reactivex.Single

@Dao
interface UserDao{
    @Insert
    fun insertUser(user:User):Long
    @Query("select * from User where User.id= :userId")
    fun getUser(userId:String):User
}