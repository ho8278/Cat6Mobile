package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class VoteItem(@PrimaryKey
                    @SerializedName("vote_item_ID")
                    var id:String,
                    @SerializedName("vote_item_name")
                    var name:String,
                    @SerializedName("number_of_vote")
                    var selected:Int,
                    @SerializedName("vote_item_vote_ID")
                    var voteID:String)