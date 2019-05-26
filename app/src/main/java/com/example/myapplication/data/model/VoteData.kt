package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class VoteData(@SerializedName("vote")
                    var vote:Vote,
                    @SerializedName("vote_item")
                    var voteList:List<VoteItem>)