package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(@SerializedName("client_id")
                @PrimaryKey
                var id:String,
                @SerializedName("client_password")
                var password:String,
                @SerializedName("client_name")
                var name:String,
                @SerializedName("client_nickname")
                var nickname:String,
                @SerializedName("client_token")
                var token:String,
                @SerializedName("profile_picture")
                var profileLink:String)