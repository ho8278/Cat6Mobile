package com.example.myapplication.view.main

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.data.model.Team
import com.example.myapplication.data.model.User
import com.example.myapplication.view.base.BaseViewModel

class MainViewModel: BaseViewModel {

    constructor(dataManager: DataSource):super(dataManager)

    val TAG=MainViewModel::class.java.simpleName

    val chatList=ObservableArrayList<ChatRoom>()

    val userList=ObservableArrayList<User>()

    val currentUser= ObservableField<User>()

    fun init(){
        getCompositeDisposable().add(
            getDataManager().loadChatRoom()
                .subscribe({ list ->
                    chatList.clear()
                    chatList.addAll(list)
                    getDataManager().saveItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID,list[0].id)
                    getDataManager().subscribeTopic(list)
                    Log.e(TAG, list.toString())
                },{
                    Log.e(TAG,it.message)
                })
        )
    }
}