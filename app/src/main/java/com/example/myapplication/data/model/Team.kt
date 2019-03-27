package com.example.myapplication.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Team(@SerializedName("team_id")
                val id:String,
                @SerializedName("team_name")
                val name:String)