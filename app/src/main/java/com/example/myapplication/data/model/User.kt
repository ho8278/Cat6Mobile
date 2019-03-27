package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(@SerializedName("client_id")
                @PrimaryKey
                val id:String,
                @SerializedName("client_password")
                val password:String,
                @SerializedName("client_name")
                val name:String,
                @SerializedName("client_nickname")
                val nickname:String,
                @SerializedName("profile_picture")
                val profileLink:String)