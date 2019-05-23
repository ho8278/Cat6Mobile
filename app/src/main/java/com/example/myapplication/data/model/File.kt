package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class File(@PrimaryKey
                @SerializedName("fileName")
                var link:String,
                @SerializedName("fileDownloadUri")
                var uri:String,
                @SerializedName("fileType")
                var type:String,
                @SerializedName("size")
                var size:Int)