package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class File(@PrimaryKey
                @SerializedName("file_link")
                var link:String,
                @SerializedName("file_date")
                var date:Date)