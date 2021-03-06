package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.model.User

@Dao
interface UserDao{
    @Insert
    fun insertUser(user:User)
    @Insert
    fun insertUser(list:List<User>)

    @Query("delete from User")
    fun deleteUserAll()

    @Transaction
    fun updateUser(user:User){
        deleteUserAll()
        insertUser(user)
    }

    @Transaction
    fun updateUser(list:List<User>){
        deleteUserAll()
        insertUser(list)
    }

    @Query("select * from User where User.id= :userId")
    fun getUser(userId:String):User
}