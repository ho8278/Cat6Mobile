package com.example.myapplication.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Notice(@SerializedName("notice_id")
                  val id:String,
                  @SerializedName("notice_contents")
                  val content:String)