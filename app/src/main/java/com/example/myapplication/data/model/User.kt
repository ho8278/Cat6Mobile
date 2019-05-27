package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(@SerializedName("client_ID")
                @PrimaryKey
                var id:String,
                @Ignore
                @SerializedName("client_password")
                var password:String,
                @SerializedName("client_name")
                var name:String,
                @SerializedName("client_nickname")
                var nickname:String,
                @Ignore
                @SerializedName("profile_picture")
                var profileLink:String =""){
    constructor():this("","","","")
}