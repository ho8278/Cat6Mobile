package com.example.myapplication.view.addvote

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Vote
import com.example.myapplication.data.model.VoteItem
import com.example.myapplication.view.addschedule.AddNavigator
import com.example.myapplication.view.base.BaseViewModel
import com.example.myapplication.view.main.ErrorCode

class AddVoteViewModel : BaseViewModel {
    constructor(dataSource: DataSource, listener:AddNavigator? =null) : super(dataSource){
        this.listener=listener
    }
    constructor(dataSource: DataSource, voteID:String, listener:AddNavigator):super(dataSource){
        getVoteInfo(voteID)
        this.voteID=voteID
        this.listener = listener
    }

    lateinit var voteID: String
    val voteTitle = ObservableField<String>()
    val isPlural = ObservableBoolean()
    val startDate = ObservableField<String>()
    val endDate = ObservableField<String>()
    val voteItemList =  ObservableArrayList<VoteItem>()
    var listener:AddNavigator? = null
    val TAG = AddVoteViewModel::class.java.simpleName
    val isParticipate = ObservableBoolean(false)

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
                    Log.e(TAG,ErrorCode.fromCode(it.toInt()).description)
                },{
                    Log.e(TAG,it.message)
                    val errorCode = ErrorCode.fromCode(it.message?.toInt()?:200)
                    listener?.OnSaveFail(errorCode)
                },{
                    listener?.OnSaveSuccess()
                })
        )
    }

    fun getVoteInfo(voteID:String){
        getCompositeDisposable().add(
            getDataManager().loadDetailVote(voteID)
                .subscribe({
                    voteTitle.set(it.title)
                    isPlural.set(it.duplicate=="1")
                    startDate.set(it.startDate)
                    endDate.set(it.endDate)
                    if(it.select==1)
                        isParticipate.set(true)
                    voteItemList.clear()
                    voteItemList.addAll(it.itemList)
                },{
                    Log.e(TAG,it.message)
                })
        )
    }

    fun acceptVote(list:List<Boolean>){
        if(list.size!=voteItemList.size){
            Log.e(TAG,"Size not match!")
            return
        }

        val IDList = voteItemList.filterIndexed { index, _ ->
            list[index]==true
        }.map { it.id }

        Log.e(TAG,IDList.toString())

        getCompositeDisposable().add(
            getDataManager().acceptVote(voteID,IDList)
                .subscribe({
                    Log.e(TAG,it.description)
                    listener?.OnSaveSuccess()
                },{
                    Log.e(TAG,it.message)
                })
        )
    }
}