package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class VoteItem(@PrimaryKey
                    var id:String,
                    @SerializedName("vote_item_name")
                    var name:String,
                    @SerializedName("number_of_votes")
                    var selected:Int,
                    var voteID:String)