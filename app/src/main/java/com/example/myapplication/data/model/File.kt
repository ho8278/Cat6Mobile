package com.example.myapplication.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class File(@SerializedName("file_link")
                val link:String,
                @SerializedName("file_date")
                val date:Date)