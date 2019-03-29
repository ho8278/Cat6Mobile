package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ChatRoom(@PrimaryKey
                    @SerializedName("chat_room_id")
                    var id:String,
                    @SerializedName("chat_room_name")
                    var name:String)