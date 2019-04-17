package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Team(@PrimaryKey
                @SerializedName("team_ID")
                var id:String,
                @SerializedName("team_name")
                var name:String)