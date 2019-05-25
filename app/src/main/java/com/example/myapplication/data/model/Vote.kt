package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Vote(@PrimaryKey
                @SerializedName("vote_ID")
                var id:String,
                @SerializedName("vote_title")
                var title:String,
                @SerializedName("vote_start_date")
                var startDate: String,
                @SerializedName("vote_end_date")
                var endDate:String,
                @SerializedName("vote_duplicate")
                var isDuplicate:String,
                @SerializedName("chat_room_ID")
                var chatRoomID:String)