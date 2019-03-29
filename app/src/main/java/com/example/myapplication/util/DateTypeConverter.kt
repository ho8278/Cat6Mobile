package com.example.myapplication.util

import androidx.room.TypeConverter
import java.util.*

object DateTypeConverter{
    @TypeConverter
    @JvmStatic
    fun toDate(value:Long):Date{
        return Date(value)
    }

    @TypeConverter
    @JvmStatic
    fun toLong(date:Date):Long{
        return date.time
    }
}