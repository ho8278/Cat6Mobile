package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Vote(@PrimaryKey
                @SerializedName("vote_ID")
                var id:String="",
                @SerializedName("vote_title")
                var title:String="",
                @SerializedName("vote_start_date")
                var startDate: String="",
                @SerializedName("vote_end_date")
                var endDate:String="",
                @SerializedName("vote_duplicate")
                var duplicate:String="",
                @SerializedName("chat_room_ID")
                var chatRoomID:String="",
                @SerializedName("vote_item")
                @Ignore
                var itemList:MutableList<VoteItem> = mutableListOf(),
                var select:Int=0)