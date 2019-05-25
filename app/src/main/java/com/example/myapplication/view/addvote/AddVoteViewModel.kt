package com.example.myapplication.view.addvote

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Vote
import com.example.myapplication.view.addschedule.AddScheduleNavigator
import com.example.myapplication.view.base.BaseViewModel
import com.example.myapplication.view.main.ErrorCode

class AddVoteViewModel : BaseViewModel {
    constructor(dataSource: DataSource, listener:AddScheduleNavigator) : super(dataSource){
        this.listener=listener
    }

    val voteTitle = ObservableField<String>()
    val isPlural = ObservableBoolean()
    val startDate = ObservableField<String>()
    val endDate = ObservableField<String>()
    val voteItemList =  ObservableArrayList<String>()
    val isEditable = ObservableBoolean(true)
    lateinit var listener:AddScheduleNavigator


    fun setEditable(){
        isEditable.set(false)
    }

    fun saveVote(list:MutableList<String>){
        Log.e("AddVoteViewModel",voteTitle.get())
        Log.e("AddVoteViewModel",isPlural.get().toString())
        Log.e("AddVoteViewModel",startDate.get())
        Log.e("AddVoteViewModel",endDate.get())
        Log.e("AddVoteViewModel",list.toString())

        val isDuplicate:String = if(isPlural.get()){
            "1"
        }else{
            "0"
        }

        getCompositeDisposable().add(
            getDataManager().createVote(Vote("",voteTitle.get()?:"",startDate.get()?:"",endDate.get()?:"",isDuplicate,""),list)
                .subscribe({
                    Log.e("AddVoteViewModel",ErrorCode.fromCode(it.toInt()).description)
                },{
                    Log.e("AddVoteViewModel",it.message)
                    val errorCode = ErrorCode.fromCode(it.message?.toInt()?:200)
                    listener.OnSaveFail(errorCode)
                },{
                    listener.OnSaveSuccess()
                })
        )
    }
}