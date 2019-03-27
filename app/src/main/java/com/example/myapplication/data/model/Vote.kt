package com.example.myapplication.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Vote(@SerializedName("vote_title")
                val title:String,
                @SerializedName("vote_start_date")
                val startDate: Date,
                @SerializedName("vote_end_date")
                val endDate:Date,
                @SerializedName("vote_duplicate")
                val isDuplicate:Boolean)