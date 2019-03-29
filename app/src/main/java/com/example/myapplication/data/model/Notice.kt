package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Notice(@PrimaryKey
                  @SerializedName("notice_id")
                  var id:String,
                  @SerializedName("notice_contents")
                  var content:String)