package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class VoteServerResponse(@SerializedName("result")
                              val responseCode: String,
                              @SerializedName("data")
                              var data:VoteData
){
    override fun toString(): String {
        return "code: $responseCode, $data"
    }
}