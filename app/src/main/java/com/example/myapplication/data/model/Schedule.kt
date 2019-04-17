package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.OffsetDateTime
import java.util.*

@Entity//(foreignKeys = arrayOf(ForeignKey(entity = Team::class, parentColumns = arrayOf("id"), childColumns = arrayOf("teamID"))))
data class Schedule(@PrimaryKey
                    @SerializedName("schedule_ID")
                    val id:String,
                    @SerializedName("schedule_startDate")
                    val startDate: String,
                    @SerializedName("schedule_endDate")
                    val endDate: String,
                    @SerializedName("schedule_contents")
                    val name:String,
                    @SerializedName("schedule_team_ID")
                    val teamID: String)