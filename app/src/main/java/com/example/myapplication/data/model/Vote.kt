package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Vote(@PrimaryKey
                @SerializedName("vote_title")
                var title:String,
                @SerializedName("vote_start_date")
                var startDate: Date,
                @SerializedName("vote_end_date")
                var endDate:Date,
                @SerializedName("vote_duplicate")
                var isDuplicate:Boolean)