package com.example.myapplication.data.local.db.dao

import androidx.room.*
import com.example.myapplication.data.model.Notice

@Dao
interface NoticeDao{
    @Delete
    fun deleteNotice(notice:Notice)
    @Insert
    fun insertNotice(notice:Notice)

    @Transaction
    fun updateNotice(notice:Notice){
        deleteNotice(notice)
        insertNotice(notice)
    }
}