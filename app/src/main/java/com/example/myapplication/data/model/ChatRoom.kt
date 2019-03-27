package com.example.myapplication.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class ChatRoom(@SerializedName("chat_room_id")
                    val id:String,
                    @SerializedName("chat_room_name")
                    val name:String)