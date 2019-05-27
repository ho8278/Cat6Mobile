package com.example.myapplication.view.addfriends

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.User
import com.example.myapplication.view.addschedule.AddNavigator
import com.example.myapplication.view.base.BaseViewModel

class AddFriendViewModel(dataSource: DataSource, val listener:AddNavigator):BaseViewModel(dataSource){
    val userList = ObservableArrayList<User>()

    init{
        getCompositeDisposable().add(
            getDataManager().loadGroupClient()
                .subscribe({ list ->
                    userList.addAll(list)
                },{
                    Log.e("AddFriendVieModel",it.message)
                })
        )
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