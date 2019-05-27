package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class UserServerResponse(
    @SerializedName("result")
    val responseCode: String,
    @SerializedName("data")
    var data:User
)