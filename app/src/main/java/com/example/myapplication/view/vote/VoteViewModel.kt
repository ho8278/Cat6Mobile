package com.example.myapplication.view.vote

import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Vote
import com.example.myapplication.view.base.BaseViewModel

class VoteViewModel:BaseViewModel{
    constructor(dataSource: DataSource):super(dataSource)

    val voteList = ObservableArrayList<Vote>()


}