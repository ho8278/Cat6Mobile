package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
class ChatInfo(
    @PrimaryKey
    @SerializedName("chatinfo_id")
    var id: String,
    @SerializedName("send_user_id")
    var sendUserId: String,
    @SerializedName("chatroom_id")
    var roomId: String,
    @SerializedName("send_date")
    var sendDate: Date,
    @SerializedName("message")
    var message: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatInfo

        if (id != other.id) return false
        if (sendUserId != other.sendUserId) return false
        if (roomId != other.roomId) return false
        if (sendDate != other.sendDate) return false
        if (message != other.message) return false

        return true
    }
}