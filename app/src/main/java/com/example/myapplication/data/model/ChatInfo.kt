package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class ChatInfo(
    @PrimaryKey
    @Expose
    @SerializedName("chatinfo_id")
    var chatinfo_id: String,
    @Expose
    @SerializedName("send_user_id")
    var send_user_id: String,
    @Expose
    @SerializedName("chatroom_id")
    var chatroom_id: String,
    @Expose
    @SerializedName("send_date")
    var send_date: Date,
    @Expose
    @SerializedName("message")
    var message: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatInfo

        if (chatinfo_id != other.chatinfo_id) return false
        if (send_user_id != other.send_user_id) return false
        if (chatroom_id != other.chatroom_id) return false
        if (send_date != other.send_date) return false
        if (message != other.message) return false

        return true
    }

    override fun toString(): String {
        return "{ 'chatinfo_id': '$chatinfo_id' , 'send_user_id' : '$send_user_id' , 'chatroom_id' : '$chatroom_id' , 'send_date' : '$send_date' , 'message' : '$message' }"
    }
}