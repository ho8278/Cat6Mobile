package com.example.myapplication.view.addvote

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.view.base.BaseViewModel

class AddVoteViewModel : BaseViewModel {
    constructor(dataSource: DataSource) : super(dataSource)

    val voteTitle = ObservableField<String>()
    val isPlural = ObservableBoolean()
    val startDate = ObservableField<String>()
    val endDate = ObservableField<String>()

    fun saveVote(list:MutableList<String>){
        Log.e("AddVoteViewModel",voteTitle.get())
        Log.e("AddVoteViewModel",isPlural.get().toString())
        Log.e("AddVoteViewModel",startDate.get())
        Log.e("AddVoteViewModel",endDate.get())
        Log.e("AddVoteViewModel",list.toString())
        //TODO("오갱이 만들면 붙임")
    }
}