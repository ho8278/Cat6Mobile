package com.example.myapplication.data.local.db.dao

import androidx.room.*
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.Schedule

@Dao
interface ScheduleDao {
    @Insert
    fun insertSchedules(schedule:Schedule)

    @Insert
    fun insertSchedules(scheduleList:List<Schedule>)

    @Query("delete from Schedule")
    fun deleteAllSchedules()

    @Transaction
    fun updateSchedule(list:List<Schedule>){
        deleteAllSchedules()
        insertSchedules(list)
    }

    @Query("select * from Schedule where Schedule.startDate like :scheduleDate")
    fun getSchedules(scheduleDate:String):List<Schedule>
}