package com.example.myapplication.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.model.Schedule
import io.reactivex.Observable
import java.util.*

@Dao
interface ScheduleDao {
    @Insert
    fun insertSchedules(schedule:Schedule)

    @Insert
    fun insertSchedules(scheduleList:List<Schedule>)

    @Query("delete from Schedule")
    fun deleteAllSchedules()

    @Query("select * from Schedule where Schedule.startDate like :scheduleDate")
    fun getSchedules(scheduleDate:String):List<Schedule>
}