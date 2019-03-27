package com.example.myapplication.data.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class VoteItem(@SerializedName("vote_item_name")
                    val name:String,
                    @SerializedName("number_of_votes")
                    val selected:Int)