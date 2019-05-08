package com.example.myapplication.data.model

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class ServerResponse<T:Any>(
    @SerializedName("result")
    val responseCode: Int,
    @SerializedName("data")
    var data:List<T>
){
    override fun toString(): String {
        return "code: $responseCode, $data"
    }
}