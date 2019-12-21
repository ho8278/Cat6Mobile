package com.example.myapplication.view.main

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.data.model.User
import com.example.myapplication.view.base.BaseViewModel

class MainViewModel(dataManager: DataSource, val listener: MainNavigator) : BaseViewModel(dataManager) {


    val TAG = MainViewModel::class.java.simpleName

    val chatList = ObservableArrayList<ChatRoom>()

    val userList = ObservableArrayList<User>()

    val userNickName = ObservableField<String>()
    val userName = ObservableField<String>()

    init {
        getCompositeDisposable().add(
            getDataManager().getCurrentUser()
                .subscribe({
                    userName.set(it.name)
                    userNickName.set(it.nickname)
                },{
                    Log.e(TAG, it.message)
                })
        )
    }

    fun updateUI() {
        getCompositeDisposable().add(
            getDataManager().loadChatRoom()
                .subscribe({
                    chatList.clear()
                    chatList.addAll(it)
                }, {
                    Log.e(TAG, it.message)
                })
        )
        getCompositeDisposable().add(
            getDataManager().loadGroupClient()
                .subscribe({ list ->
                    userList.clear()
                    userList.addAll(list)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun init() {
        getCompositeDisposable().add(
            getDataManager().loadChatRoom()
                .subscribe({ list ->
                    if (list.size == 0) {
                        listener.setChatViewModel(null)
                    }
                    else{
                        listener.setChatViewModel(list[0])
                    }
                    chatList.clear()
                    chatList.addAll(list)
                    getDataManager().subscribeTopic(list)
                }, {
                    Log.e(TAG, it.message)
                })
        )
        getCompositeDisposable().add(
            getDataManager().loadGroupClient()
                .subscribe({ list ->
                    userList.clear()
                    userList.addAll(list)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun changeTeam(teamID: String) {
        getDataManager().saveItem(PreferenceHelperImpl.CURRENT_GROUP_ID, teamID)
        init()
    }

    fun createChatRoom(client_ID: String, chatRoomName: String) {
        getCompositeDisposable().add(
            getDataManager().createChatRoom(client_ID, chatRoomName)
                .subscribe({ room ->
                    chatList.add(room)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

}