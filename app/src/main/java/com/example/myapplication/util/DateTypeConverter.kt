package com.example.myapplication.util

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

object DateTypeConverter{
    @TypeConverter
    @JvmStatic
    fun LongtoDate(value:Long):Date{
        return Date(value)
    }

    @TypeConverter
    @JvmStatic
    fun StringtoDate(value:String):Date{
        val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREAN)
        return simpleDateFormat.parse(value)
    }

    @TypeConverter
    @JvmStatic
    fun DatetoString(value:Date):String{
        val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.KOREAN)
        return simpleDateFormat.format(value)
    }

    @TypeConverter
    @JvmStatic
    fun DatetoLong(date:Date):Long{
        return date.time
    }
}