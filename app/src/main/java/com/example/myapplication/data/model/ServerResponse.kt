package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ServerResponse<T:Any>(
    @SerializedName("result")
    val responseCode: String,
    @SerializedName("data")
    var data:List<T>
){
    override fun toString(): String {
        return "code: $responseCode, $data"
    }
}