package com.example.myapplication.view.addfriends

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.User
import com.example.myapplication.view.addschedule.AddNavigator
import com.example.myapplication.view.base.BaseViewModel

class AddFriendViewModel(dataSource: DataSource, val listener:AddNavigator):BaseViewModel(dataSource){
    val userList = ObservableArrayList<User>()
    val currentChatUsers = ObservableArrayList<String>()
    val addUserList = ObservableArrayList<String>()
    init{
        getCompositeDisposable().add(
            getDataManager().loadChatClient()
                .doOnSuccess { currentChatUsers.addAll(it) }
                .flatMap { getDataManager().loadGroupClient() }
                .subscribe({ list ->
                    val filteredList = list.filter {
                        val user = it
                        currentChatUsers.filter { it==user.nickname }.isEmpty()
                    }
                    userList.addAll(filteredList)
                },{
                    Log.e("AddFriendVieModel",it.message)
                })
        )
    }

    fun addUserChange(list:List<String>){
        addUserList.clear()
        addUserList.addAll(list)
    }

    fun setUserList(list:MutableList<User>){
        list.forEach {
            getCompositeDisposable().add(
                getDataManager().inviteChatRoom(it.id)
                    .subscribe({
                        Log.e("AddFriendVieModel",it.toString())
                        listener.OnSaveSuccess()
                    },{
                        Log.e("AddFriendVieModel",it.message)
                    })
            )
        }
    }
}