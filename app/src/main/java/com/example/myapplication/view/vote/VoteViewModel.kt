package com.example.myapplication.view.vote

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Vote
import com.example.myapplication.view.base.BaseViewModel

class VoteViewModel:BaseViewModel{
    constructor(dataSource: DataSource):super(dataSource)
    val TAG = VoteViewModel::class.java.simpleName
    val voteList = ObservableArrayList<Vote>()

    fun init(){
        getCompositeDisposable().add(
            getDataManager().loadVote()
                .subscribe({
                    voteList.clear()
                    voteList.addAll(it)
                },{
                    Log.e(TAG,it.message)
                })
        )
    }

}